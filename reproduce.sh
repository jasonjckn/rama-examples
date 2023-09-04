#!/usr/bin/env -S bash -Eeuxo pipefail

mvn compile
mvn package
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp "$(cat ./cp.txt):target/rama-examples.jar" rama.examples.tutorial.Test4
