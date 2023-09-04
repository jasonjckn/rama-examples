#!/usr/bin/env -S bash -Eeuxo pipefail

mvn compile
mvn package
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
CLASSPATH="$(cat ./cp.txt)"
rm cp.txt
java -cp "$CLASSPATH:target/rama-examples.jar" rama.examples.wordcount.WordCountModule
