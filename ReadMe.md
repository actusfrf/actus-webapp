# Actus demo web app
This demo will show you an easy application of the actus-core library to evaluate demo contracts.

## Getting started
These instructions will get you a copy of the demo up and running on your local machine for testing purposes.

### Prerequesits

* Install [Maven](https://maven.apache.org/).
* The Actus core library has to be built to your local maven repository.

### Building the actus-core library

* Verify your [Maven installation](https://maven.apache.org/install.html).
* Get access and pull actus-core from [Actusfrf.org](https://www.actusfrf.org/developers).
* Navigate to your actus-core folder and execute the following snippet:

```
mvn clean install -Dmaven.test.failure.ignore=true
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

```
chmod +x gradlew
gradlew
```

You can now build and rund your app

