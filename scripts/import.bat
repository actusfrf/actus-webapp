@echo off
for %%f in (*.json) do (
    "mongoimport.exe" --db actusweb --collection forms --file %%~nf.json
)