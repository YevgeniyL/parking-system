# Parking system

Spring-boot REST application.
  - Spring-boot 2.0.5 
  - Spring-web
  - Spring-data-jpa
  - Hibernate 5
  - H2
  - JUnit5 jupiter
  - Liquibase
  - P6spy 
  - Maven
  - JDK 1.8


# REST application for parking management system
  - Adding new parking lot
  - Create user session by license plate number
  - Close session by license plate number with sending invoice by email
  - Calculating total price for parking session
# You can:
  - Configurate price engine
  - Buld with default profile "h2" for profuction
  - Build with develoment profile "h2-dev" for testing with pre generated data
#
#

> Not shure that all good in this application :) ,
> but i try to use patterns DDD, TDD, SOLID and hibernate recomendation for use him.  
#
# Installation
Building with any profile:
```
mvn clean package -P h2
mvn clean package -P h2-dev
```
Jar-package will be in root project folder with name "parking-system-{version}-{profile}.jar"

# Run:
```
java -jar parking-system-1.0.0-SNAPSHOT-h2.jar
```

# Using:
#
### Default address:
```sh
localhost:8000
servlet.path=/pms
```
#
### Create new/update parking lot:
- POST _http://localhost:8080/pms/v1/management/parkinglots_
- **request body:**
```
{  
    "address":"{address}",   
    "isEnabled":{isEnabled} 
}
```
- **Header:** Content-Type: application/json
- {address} - exist parking lot address
- {isEnabled} - set active in parking system for this parking lot (true or false)
- **Response code:** 200

#
### Start new parking session:
- POST _http://localhost:8080/pms/v1/assets/{address}/sessions_
- **request body:**
```
{   
    "licensePlateNumber": "{scanned number}" 
}
```
- **Header:** Content-Type: application/json
- {address} - exist perking lot address
- {scanned number} - licanse plate number, need will be filled in user account settings in parking system
- **Response code:** 200

#
### Closing current parking session, giving a total data and sending invoise by email:
- POST _http://localhost:8080/pms/v1/assets/{address}/vehicle/{scannedNumber}/session_
 - **request body:**
```
{    
    "status": "stopped" 
}
```
- **Header:** Content-Type: application/json
- {address} - exist perking lot address
- {scanned number} - licanse plate number, need will be filled in user account settings in parking system
- **Response code:** 200
#
- **Response body:**
```
{    
    "status":"stopped",
    "total":3.00,
    "startedAt":"2018-10-16T12:04:03.871",
    "stoppedAt":"2018-10-16T12:05:47.512"
}
```
