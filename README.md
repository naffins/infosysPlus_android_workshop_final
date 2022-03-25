# Infosys+ Android Workshop 2022: Databases

On 25 March 2022 the ISTD Senior Student Board conducted a workshop on how to setup and integrate a stack for using an external database with an Android app, consisting of a SQL database, a REST API web server and the app. Covered technologies included MySQL, FastAPI and Android Java programming.

### Repository structure

- /StudentRegistry contains the completed Android app used for this workshop
- /api contains the FastAPI web server code
- /sql_docker contains a Docker compose file for the MySQL server container used

### Workshop resources

- [Slides](https://docs.google.com/presentation/d/16GJkgiQ9oRC_JZBGMvpowuVJpXY9cDb7Pk0-8GO3H7M/edit#slide=id.g11ecb63cae4_0_352)
- [Workshop Recording (Twitch, expires 2 weeks after workshop)](https://www.twitch.tv/videos/1436027311)
- [Workshop Recording (Sharepoint, requires SUTD account)](https://sutdapac-my.sharepoint.com/:v:/g/personal/sean_gunawan_mymail_sutd_edu_sg/EdIy_CTkfktKjoFleto-x1UBSWuQs4aooSiAh8s0UV7EWA?e=fOffWg)
- [Starter code](https://github.com/naffins/infosysPlus_android_workshop_starter)
- Setup guide: see Android Workshop Setup Instructions.pdf

### Additional resources that you might find useful

#### SQL/MySQL
- [Summary of common SQL commands (see end of page)](https://www.w3schools.com/sql/sql_syntax.asp)
- [JOIN keywords guide (keywords to combine rows from multiple tables, and make use of relations between tables)](https://www.w3schools.com/sql/sql_join.asp)

#### REST API and FastAPI
- [FastAPI docs](https://fastapi.tiangolo.com/)
- [Intro on REST APIs](https://www.redhat.com/en/topics/api/what-is-a-rest-api)
- [Intro on HTTP methods in REST API context](https://www.restapitutorial.com/lessons/httpmethods.html)
- [HTTP response status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)

#### Making HTTP requests in Java/Android
- [Guide on making JSON POST request in Java using HttpURLConnection](https://www.baeldung.com/httpurlconnection-post)

### Acknowledgements

I would like to Shoham from the ISTD Senior Student Board for helping to prepare FastAPI and Docker container codes for the workshop, and Filbert (also from the Student Board) for helping to teach participants at the workshop's physical venue.