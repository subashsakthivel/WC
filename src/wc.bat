@echo off
set path=C:\Program Files\Java\jdk-17.0.1\bin

javac wc/packages/WcAPI.java

java wc/packages/WcAPI %*
