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

## Route
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

    print(id,name,year,is_undergraduate,is_vaccinated)
    print(filters)
    filter = " AND ".join(filters)

    # Compose query
    sql = f"SELECT * FROM {table_name}{' WHERE ' + filter if len(filter)>0 else ''};"

    # closes the connection when done
    with connection.cursor() as cursor: 
        try:
            # Make query and return results
            cursor.execute(sql)
            reply = cursor.fetchall()
            for i in reply:
                i["is_undergraduate"] = i["is_undergraduate"]==1
                i["is_vaccinated"] = i["is_vaccinated"]==1
            print(reply)
            return reply
        except Exception as e:
            response.status_code = 500
            return SQL_ERROR

# Route for ModifyStudentListActivity add function

## Template for POST request body
class ModifyStudentListActivity_Add_Template(BaseModel):
    id: int
    name: str
    year: int
    is_undergraduate: int
    is_vaccinated: int

## Route
@app.post("/add_student")
async def modifyStudentListActivity_add(response: Response, student_data:ModifyStudentListActivity_Add_Template):

    sql = f"INSERT INTO {table_name} (id, name, year, is_undergraduate, is_vaccinated) VALUES ({student_data.id}, \"{student_data.name}\", {student_data.year}, {student_data.is_undergraduate}, {student_data.is_vaccinated});"

    try:
        with connection.cursor() as cursor:
            cursor.execute(sql)
        connection.commit()
    except Exception as e:
        connection.rollback()
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
        with connection.cursor() as cursor:
            cursor.execute(sql)
            connection.commit()

            # Check number of affected rows
            if (cursor.rowcount==0):

                # If no rows affected then ID didnt exist to begin with
                response.status_code = 400
                return {"error": "id_not_found"}
            return {"success": True}
    except Exception as e:
        response.status_code = 500
        return SQL_ERROR

if __name__=="__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)