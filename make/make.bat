REM DOS Makefile for compiling and installing servlets
REM
REM usage:
REM	java3 make.bat
REM
REM Configuring:
REM 	point the global variables to the appropriate locations

set make_old_cp=%CLASSPATH%

set CLASSPATH=.;%CLASSPATH%

cd ..\src
deltree /Y ..\class
mkdir ..\class
deltree /Y ..\jar
mkdir ..\jar
@echo ON
javac -deprecation -g -d ../class *.java
cd ..\class
jar cvf ../jar/TimeMotionTimer.jar .
cd ..\make
deltree /Y ..\class
@echo OFF

set CLASSPATH=%make_old_cp%
set make_old_cp=
