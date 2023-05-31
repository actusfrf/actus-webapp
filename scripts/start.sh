#!/bin/bash

echo "Starting MongoDB"
# start mongodb
`service mongod start`

echo "Migrating forms and demos data to MongoDB"
# list of all form and demo files
demos=$(find ./data/demos/json -name '*.json')

# mongo db for actus-webappV101 = actusrfs ( replacing actusweb )
# remove database
`mongo actusrfs --eval "printjson(db.dropDatabase())"`

# add new demos collection
for f in $demos; do
	echo "Adding demo " $f
	`mongoimport --db actusrfs --collection demos --file $f`
        done

echo "Starting the app"
echo "start the actus-webapp using:"
echo "./gradlew bootRun"

