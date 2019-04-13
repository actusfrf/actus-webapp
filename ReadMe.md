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

## Set up local environment

Start services and set up the database for running the app locally by running the start script in your terminal (needs sudo privileges)

```sh
# actus-webapp/
sudo sh ./scripts/start.sh
```

## Build and run the app

Build and run the app through the following commands in your terminal

```sh
# actus-webapp/
chmod +x gradlew
./gradlew build
./gradlew bootRun
```

Yey, you made it to the first level - the backend to the ACTUS App is now running!


## Test REST endpoints

In a fresh terminal execute the following commands

```sh
# Fetch terms for the PAM contract
curl -i -H "Accept: application/json" localhost:8080/terms/meta/PAM
```

```sh
# Fetch demo data for PAM contract
curl -i -H "Accept: application/json" localhost:8080/demos/meta/PAM
```


### Build and run the frontend

Welcome to level 2! Now you are going to build and run the frontend to the ACTUS App.

First, navigate to the frontend root folder

```sh
# /actus-webapp
cd ./frontend
```

Then, build and start the frontend

```sh
# /frontend
npm run build
npm start
```

Now, open your browser and the app through url <a href="http://localhost:3000">http://localhost:3000</a>.

