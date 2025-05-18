@echo off
chcp 65001 > nul
title Redis-Server-WithAuth
cd /d "D:\Major\Project\Redis-x64-3.2.100"
start redis-server.exe redis.windows.conf --requirepass "root@123456"
