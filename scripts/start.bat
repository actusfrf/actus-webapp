@echo off
echo Starting MongoDB
:: start mongodb
net start MongoDB

:: remove database
mongo actusweb --eval "printjson(db.dropDatabase())"

:: add new demos collection
for /R .\data\demos\json %%G in (*.json) do (
    echo Adding demo %%G
    mongoimport --db actusweb --collection demos --file "%%G"
)


echo Starting the app
echo start the actus-webapp using:
echo .\gradlew.bat bootRun