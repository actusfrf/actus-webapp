#!/bin/bash

echo "Starting MongoDB"
# start mongodb
`service mongod start`

echo "Migrating forms and demos data to MongoDB"
# list of all form and demo files
forms=$(find ./misc/forms/json -name '*.json')
demos=$(find ./misc/demos/json -name '*.json')

# remove database
`mongo actusweb --eval "printjson(db.dropDatabase())"`

# add new demos collection
for f in $demos; do
	echo "Adding demo " & $f
	`mongoimport --db actusweb --collection demos --file $f`
done

# add new forms collection
for f in $forms; do
	echo "Adding demo " & $f
	`mongoimport --db actusweb --collection forms --file $f`
done
