[![Build Status](https://travis-ci.org/rselvanathan/romesh-rest.svg?branch=master)](https://travis-ci.org/rselvanathan/romesh-rest)

## Scala Play 2 Rest API 

A Rest Service implemented using Play 2 and Scala. This service powers a few of my private Projects (_my_page_ and _romcharm-app_ 
currently). This is a migration project from the original Java Spring Boot application : https://github.com/rselvanathan/romcharm-rest 

#### Features
 - Integrated with AWS SNS to send notifications for automated mailing
 - JWT token authentication
 - Role based API security (with JWT token)
 - AWS DynamoDB data storage
 
#### Tech Used 
  - Play 2, AWS DynamoDB, AWS SNS, JWT Tokens, Docker
  
#### Docker Usage
  
The REST Service is built and deployed as a docker image currently. To run the service simply use
this command :
  
```bash
docker run -d --name romesh-rest -p 9000:9000 \
-e JWTSECRET= \
-e AWS_ACCESS_KEY_ID= \
-e AWS_SECRET_ACCESS_KEY= \
-e AWS_EMAIL_SNS_TOPIC= \
-e APP_TYPE=ROMCHARM \
-e PLAY_SECRET= \
-it rselvanathan/romesh-rest:latest
```
  
The missing fields must be filled in by the user.

##### Migration Reason

The main reason for migrating from Spring Boot/Java to Scala/Play 2 was the memory consumption of the application. The docker container
memory usage of the play 2/scala app was much lower than the Spring Boot/Java version and due to limited RAM space in my hosted service (Digital Ocean),
I had decided to switch the app to Play 2.

###### Author 

Romesh Selvanathan