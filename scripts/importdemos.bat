@echo off
for %%f in (*.json) do (
    "mongoimport.exe" --db actusweb --collection demos	--file %%~nf.json
)