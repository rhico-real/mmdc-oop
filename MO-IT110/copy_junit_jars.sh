#!/bin/bash

# Copy JUnit 5 JARs to libs folder
cp "/Users/systech/Downloads/junit jars/junit-jupiter-api-5.10.0.jar" "/Users/systech/Documents/mmdc/aoop/mmdc-oop/MO-IT110/libs/"
cp "/Users/systech/Downloads/junit jars/junit-jupiter-engine-5.10.0.jar" "/Users/systech/Documents/mmdc/aoop/mmdc-oop/MO-IT110/libs/"
cp "/Users/systech/Downloads/junit jars/junit-platform-engine_1.10.0.jar" "/Users/systech/Documents/mmdc/aoop/mmdc-oop/MO-IT110/libs/junit-platform-engine-1.10.0.jar"
cp "/Users/systech/Downloads/junit jars/junit-platform-launcher_1.10.0.jar" "/Users/systech/Documents/mmdc/aoop/mmdc-oop/MO-IT110/libs/junit-platform-launcher-1.10.0.jar"

echo "JUnit JARs copied to libs folder"
echo "You still need to download junit-platform-commons-1.10.0.jar"
