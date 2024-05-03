#!/bin/bash
mkdir -p ../bin
javac -d ../bin *.java
java -cp ../bin src.DensuGUI
