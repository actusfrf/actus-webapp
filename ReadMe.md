# Actus demo web app
This demo will show you an easy application of the actus-core library to evaluate demo contracts.

## Getting started
These instructions will get you a copy of the demo up and running on your local machine for testing purposes.

## Requirements

* java: 8(+)
* mongodb: 5(*)
* maven: 3(+)
* gradle: 11(+)


1. build actus-core dependency

* Request an auth-token for access to the actus-core library through [Actusfrf.org](https://www.actusfrf.org/developers).
* Build actus-core dependency and add to your local maven repository

```sh
# actus-core/
mvn clean install -Dmaven.test.failure.ignore=true
```

3. build app

Navigate to the actus-webapp root folder and execute

```sh
# actus-webapp/
chmod +x gradlew
./gradlew
```

4. start third party services

```sh
sudo service mongod start
```

4. run app


```sh
# actus-webapp
./gradlew bootRun
```







### Add the actus-core dependencies to your app

This step depends on your build management tools. The following steps are going to help you if you use **Gradle**.

Append the **settings.gradle** file in your project folder.

```
include   ':actus-core'
```

Append the **build.gradle** file in your project folder.

```
repositories {
	mavenLocal()
}
```

```
dependencies {
	compile 'org.actus:actus-core:1.0-SNAPSHOT'
}
```

You might have to edit the dependency string according to the current **actus-core version**.
For this information check the **pom.xml** file in your actus-core project folder and edit accordingly.

Run the gradle wrapper. In your project directory:

```sh
# actus-webapp
chmod +x gradlew
./gradlew
```

You can now build and rund your app



<<<<<<< HEAD
# Requirements


Java 8+
Spring Boot
MongoDB
Node
NPM
create-react-app
react-router-dom


# Run the application

In your terminal execute

```sh
# start MongoDB
sudo service mongod start
```



# Build and run the application

```sh
./gradlew bootRun
```

# Test REST endpoints

In another terminal execute

```sh
# Fetch meta data for PAM contract
curl -i -H "Accept: application/json" localhost:8080/terms/meta/pam
```


curl -i -H "Accept: application/json" http://190.141.20.26/demos
=======
### Run Frontend

Node js [NodeJS](https://nodejs.org/en/) must be installed. 

cd ./frontend

npm start -- if it doesn't start, probably need to download packages

npm update


### Setup Mongo DB

App needs two collections to run. 

Forms and Demos

Look in folders containing jsons. They have an import.bat that loads data into their respective collections. 

Make sure mongoimport.exe is in the system path.


>>>>>>> 5c7c6f72ed8b94d2276b2f52748db192ae86b2a1
