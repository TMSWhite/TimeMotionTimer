REM DOS Makefile for testing GSRFViewer
REM
REM usage:
REM	java2 run.bat
REM
REM Configuring:
REM 	point the global variables to the appropriate locations

REM *** Global Variables ***
set XML4J=.\lib\xml4j.jar
REM ************************

set make_old_cp=%CLASSPATH%

set CLASSPATH=tmt.jar;%XML4J%;%CLASSPATH%

copy ..\src\TimeMotionTimer.htm ..\class

cd ..\class
@echo ON
appletviewer TimeMotionTimer.htm
@echo OFF

cd .\make

set CLASSPATH=%make_old_cp%
set make_old_cp=
