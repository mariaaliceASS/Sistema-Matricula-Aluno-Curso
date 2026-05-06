#!/bin/bash
javac -cp "lib/mysql-connector-j-9.7.0.jar" -d bin src/util/*.java src/aluno/*.java src/curso/*.java src/matricula/*.java src/Main.java
java -cp "bin:lib/mysql-connector-j-9.7.0.jar" Main
