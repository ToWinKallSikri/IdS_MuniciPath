@echo off
start cmd /k "cd backend && java -jar Municipath-0.0.1.jar"
timeout /t 10
start cmd /k "code . && ng serve --open"
