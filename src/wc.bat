@echo off
set path=C:\Program Files\Java\jdk-17.0.1\bin

javac wc/packages/WcMain.java

java wc/packages/WcMain %*
