@echo off
chcp 65001 >nul
title 职业选手管理系统 - 一键启动
echo ==========================================
echo   职业选手管理系统 - 一键启动
echo ==========================================
echo.

rem === 1. 检查并启动后端 8081 ===
netstat -ano | findstr ":8081" | findstr "LISTENING" >nul
if %errorlevel%==0 (
    echo [√] 后端 8081 已在运行，跳过启动
) else (
    echo [..] 正在启动后端 8081 ...
    start "职业选手系统后端" /min cmd /c "cd /d D:\mycode\Github1\vhr\vhr\vhrserver\vhr-web && java -jar target\vhr-web-0.0.1-SNAPSHOT.jar --server.port=8081"
    echo [..] 等待后端启动（约 10 秒）...
    timeout /t 10 /nobreak >nul
    netstat -ano | findstr ":8081" | findstr "LISTENING" >nul
    if %errorlevel%==0 (
        echo [√] 后端启动成功
    ) else (
        echo [×] 后端启动失败，请检查日志
    )
)
echo.

rem === 2. 启动 cpolar 隧道 ===
echo [..] 正在启动 cpolar 隧道（映射 8081）...
echo [提示] 下方新弹出的窗口会显示公网地址（https://xxxxx.r5.cpolar.top）
echo.

rem 直接在当前窗口启动 cpolar，便于直接看到地址
cd /d D:\my_envirenment
cpolar http 8081

echo.
echo ==========================================
echo   隧道已停止（窗口被关闭）
echo   下次访问请重新运行本脚本
echo ==========================================
pause
