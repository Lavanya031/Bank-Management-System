@echo off
echo ===================================
echo Bank Management System - Launcher
echo ===================================
echo.

REM Compile the project
echo [1/2] Compiling...
javac -cp "lib/mysql-connector-j-9.4.0.jar" -d out src/Main.java src/db/DatabaseConnection.java src/model/Account.java src/service/BankService.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo [2/2] Compilation successful!
echo.

REM Run the application
echo Starting Bank Management System...
echo.
java -cp "out;lib/mysql-connector-j-9.4.0.jar" Main

pause
