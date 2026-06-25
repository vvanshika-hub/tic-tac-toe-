@echo off
:: ==========================================
:: RUN SCRIPT: Minecraft Mob Battle
:: ==========================================
echo ==========================================
echo Launching Minecraft Mob Battle...
echo ==========================================

:: Run Main class
java -cp "bin;lib/*" com.minecraft.mobbattle.Main

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Game closed with error or JVM not found.
    pause
    exit /b %errorlevel%
)
