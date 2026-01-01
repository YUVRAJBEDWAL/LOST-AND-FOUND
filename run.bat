@echo off
echo Building Campus Lost & Found Project...
call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Check errors above.
    pause
    exit /b 1
)

echo.
echo Starting Tomcat Server...
echo Open browser: http://localhost:8080/campus-lost-found/login.jsp
echo Press Ctrl+C to stop the server
echo.

call mvn tomcat7:run

