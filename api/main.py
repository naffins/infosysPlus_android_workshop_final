# connect to mysql on CLI: mysql -h localhost -P 3306 -u root -D db -p
# run this file: uvicorn main:app --reload

from fastapi import FastAPI
import pymysql.cursors

app = FastAPI()

# create database connection
connection = pymysql.connect(
    host="localhost",
    user="root",
    password="root",
    database="db",
    cursorclass=pymysql.cursors.DictCursor,
)

# "environment variables"
table_name = "students"

# basic route
@app.get("/")
async def root():
    return "Hello world"

# put because this function is idempotent
@app.put("/create_entry")
async def create_entry(
    student_id: int, name: str, year:int, is_undergraduate: bool, is_vaccinated: bool
):
    sql = f"INSERT INTO {table_name} (id, name, year, is_undergraduate, is_vaccinated) VALUES ({student_id}, \"{name}\", {year}, {is_undergraduate}, {is_vaccinated})"

    # closes the connection when done
    with connection.cursor() as cursor:
        try:
            cursor.execute(sql)
        except:
            return {"status": "FAILED", "message": "Could not execute SQL"}

    # save changes to DB (committing transaction)
    try:
        connection.commit()
    except:
        return {"status": "FAILED", "message": "Could not commit transaction"}

    return {"status": "OK"}

@app.get("/entry")
async def get_entry(student_id: int = float('-inf')):
    if student_id == float('-inf'):
        # no ID provided, return all entries
        sql = f"SELECT * FROM {table_name}"

        with connection.cursor() as cursor:
            try:
                cursor.execute(sql)
                return cursor.fetchall()
            except:
                return {"status": "FAILED", "message": "Could not execute SQL"}
    else:
        # id provided, return all matching entries if any
        sql = f"SELECT * FROM {table_name} WHERE id = {student_id}"

        with connection.cursor() as cursor:
            try:
                cursor.execute(sql)
                return cursor.fetchone()
            except:
                return {"status": "FAILED", "message": "Could not execute SQL"}