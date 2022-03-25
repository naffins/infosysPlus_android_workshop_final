# connect to mysql on CLI: mysql -h localhost -P 3306 -u root -D db -p
# run this file: uvicorn main:app --reload
from typing import Optional
from fastapi import FastAPI, Response
from pydantic import BaseModel
import uvicorn
import pymysql.cursors

app = FastAPI()

SQL_ERROR = {"error": "sql_error"}

# create database connection
connection = pymysql.connect(
    host="localhost",
    user="root",
    password="example",
    database="db",
    cursorclass=pymysql.cursors.DictCursor,
)

# "environment variables"
table_name = "students"

# Route for ConnectActivity
## Method
@app.get("/")
async def connectActivity():
    return {"success": True}

# Route for ViewStudentListActivity
## Method
@app.get("/student_list")
async def viewStudentListActivity(response:Response, id: Optional[int] = None,
    name: Optional[str] = None, year: Optional[int] = None, is_undergraduate: Optional[bool] = None,
    is_vaccinated: Optional[bool] = None):

    # Compile WHERE filter
    filters = []
    if id != None: filters.append("id={}".format(id))
    if name != None: filters.append("name=\"{}\"".format(name))
    if year != None: filters.append("year={}".format(year))
    if is_undergraduate != None: filters.append("is_undergraduate={}".format(is_undergraduate))
    if is_vaccinated != None: filters.append("is_vaccinated={}".format(is_vaccinated))

    # Join elements of WHERE filter with " AND "
    filter = " AND ".join(filters)

    # Compose command
    sql = f"SELECT * FROM {table_name}{' WHERE ' + filter if len(filter)>0 else ''};"

    # Create cursor object
    with connection.cursor() as cursor: 
        try:
            # Make query and return results (a JSON array of JSON objects containing details)
            # Note that for queries (which do not change the database state), they do not need to be committed
            cursor.execute(sql)
            reply = cursor.fetchall()

            # SQL booleans are returned as 1 or 0, so we convert to Python bool
            for i in reply:
                i["is_undergraduate"] = i["is_undergraduate"]==1
                i["is_vaccinated"] = i["is_vaccinated"]==1

            # Send back JSON array string
            return reply

        # If some error happens when fetching SQL data
        except Exception as e:
            response.status_code = 500
            return SQL_ERROR

# Route for ModifyStudentListActivity add function
## Template for POST request body
## This enforces the request to have a JSON of this format as its body
class ModifyStudentListActivity_Add_Template(BaseModel):
    id: int
    name: str
    year: int
    is_undergraduate: int
    is_vaccinated: int

## Route
@app.post("/add_student")
async def modifyStudentListActivity_add(response: Response, student_data:ModifyStudentListActivity_Add_Template):

    # Compose command
    sql = f"INSERT INTO {table_name} (id, name, year, is_undergraduate, is_vaccinated) VALUES ({student_data.id}, \"{student_data.name}\", {student_data.year}, {student_data.is_undergraduate}, {student_data.is_vaccinated});"


    try:
        # Create cursor
        with connection.cursor() as cursor:

            # Execute command
            cursor.execute(sql)
        
        # Commit command
        connection.commit()

        # Return success message
        return {"success": True}

    except Exception as e:
        # If an error occurs, reverse any pending commands
        # Strictly speaking this is not necessary in this particular case,
        # likely because we are not using transactions (multiple operations
        # done in a single atomic step), but is good practice
        connection.rollback()

        # If error is due to duplicate entry, return 400 with corresponding error
        if "Duplicate entry" in str(e):
            response.status_code = 400
            return {"error": "id_already_exists"}
        response.status_code = 500
        return SQL_ERROR
    
# Route for ModifyStudentListActivity delete function

## Route
@app.delete("/student")
async def ModifyStudentListActivity_delete(response: Response, id: int):
    
    # Compose query
    sql = f"DELETE FROM {table_name} WHERE id = {id};"

    try:
        # Create cursor
        with connection.cursor() as cursor:

            # Execute and commit command
            cursor.execute(sql)
            connection.commit()

            # Check number of affected rows
            if (cursor.rowcount==0):

                # If no rows affected then ID didnt exist to begin with
                response.status_code = 400
                return {"error": "id_not_found"}
            return {"success": True}
    except Exception as e:
        # Again, rollback any commands (redundant in this case)
        connection.rollback()
        response.status_code = 500
        return SQL_ERROR

# If this script is not run as module,
if __name__=="__main__":
    # Host server at 0.0.0.0:8000, and reload when changes to this script are detected
    uvicorn.run("__main__:app", host="0.0.0.0", port=8000, reload=True)