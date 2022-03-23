from fastapi import FastAPI
import pymysql.cursors

app = FastAPI()

# create database connection
connection = pymysql.connect(
    host="localhost",
    user="user",
    password="password",
    database="db",
    cursorclass=pymysql.cursors.DictCursor,
)

# basic route
@app.get("/")
async def root():
    return "Hello world"


@app.get("/create_entry")
async def create_entry(
    student_id: int, name: str, year: int, is_undergraduate: bool, is_vaccinated: bool
):
    with connection.cursor() as cursor:
        sql = f"hello"
