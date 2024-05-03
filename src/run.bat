@echo off
mkdir ..\bin
javac -d ..\bin *.java
java -cp ..\bin src.DensuGUI