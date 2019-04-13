# Actus demo web app
This demo will show you a simple application of the actus-core library to evaluate demo contracts.

## Getting started
These instructions will get you a copy of the demo up and running on your local machine for testing purposes.

## Requirements

* java: 8(+)
* mongodb: 5(*)
* Node
* create-react-app
* react-router-dom
* npm
* maven: 3(+)
* gradle: 11(+)
* actus-core 1.0 (+)


## Build the actus-core library

* Request an auth-token for access to the actus-core library through [Actusfrf.org](https://www.actusfrf.org/developers).
* Build actus-core dependency and add to your local maven repository

```sh
# actus-core/
mvn clean install -Dmaven.test.failure.ignore=true
```

## Setup Mongo DB

App needs two collections to run. 

Forms and Demos

Look in folders containing jsons. They have an import.bat that loads data into their respective collections. 

Make sure mongoimport.exe is in the system path.



### Run the frontend

Navigate to the frontend root folder 

```sh
cd ./frontend
```

start the frontend

```sh
npm start -- if it doesn't start, probably need to download packages
```


## Build the app

Navigate to the actus-webapp root folder and execute

```sh
# actus-webapp/
chmod +x gradlew
./gradlew
```

Start third party services

```sh
sudo service mongod start
```

Run app

```sh
# actus-webapp
./gradlew bootRun
```


## Test REST endpoints

In another terminal execute

```sh
# Fetch meta data for PAM contract
curl -i -H "Accept: application/json" localhost:8080/terms/meta/pam
```

```sh
# Fetch demo data for PAM contract
curl -i -H "Accept: application/json" localhost:8080/demos/pam
```

