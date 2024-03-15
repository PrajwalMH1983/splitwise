# splitwise

# First we will have to create a database in postgres using the command:

CREATE DATABASE split
WITH
OWNER = postgres
ENCODING = 'UTF8'
CONNECTION LIMIT = -1
IS_TEMPLATE = False;

# Then to run the application we will have to first build the project using the command :

 mvn clean install

# Then to run the project
 
mvn spring-boot:run

# This should get the project Running 