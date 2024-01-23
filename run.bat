@echo off
start cmd.exe /K "cd backend\spring\Municipath\target && java -jar Municipath-0.0.1.jar"
start cmd.exe /K "cd frontend\angular\municipath && code . && ng serve --open"