REM DOS Makefile for compiling and installing servlets
REM
REM usage:
REM	java2 make.bat
REM
REM Configuring:
REM 	point the global variables to the appropriate locations

REM *** Global Variables ***
set XML4J=..\lib\xml4j.jar
REM ************************

set make_old_cp=%CLASSPATH%

set CLASSPATH=%XML4J%;%CLASSPATH%

cd ..\src
mkdir ..\class
@echo ON
javac -deprecation -g -d ..\class *.java
cd ..\class
jar cvf ..\tmt.jar .
@echo OFF

cd ..\make

set CLASSPATH=%make_old_cp%
set make_old_cp=
