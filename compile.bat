@echo off
:: ==========================================
:: COMPILE SCRIPT: Minecraft Mob Battle
:: ==========================================
echo ==========================================
echo Compiling Minecraft Mob Battle...
echo ==========================================

:: Create bin folder if not exists
if not exist bin (
    mkdir bin
)

:: Compile classes
javac -cp "lib/*" -d bin src/com/minecraft/mobbattle/*.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed! Make sure JDK is installed and 'javac' is in your PATH.
    pause
    exit /b %errorlevel%
)

echo.
echo [SUCCESS] Compilation complete. You can now use run.bat to launch the game!
pause
