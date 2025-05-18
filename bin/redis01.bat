@echo off
chcp 65001 > nul
title Redis-Server-NoAuth
cd /d "D:\Major\xiangmu\Redis-x64-3.2.100"
start redis-server.exe redis.windows.conf --requirepass ""
