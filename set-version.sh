#!/bin/bash

read -p "version: " ver

mvn versions:set -DnewVersion=$ver -Pjdk-8,jdk-17

# after this, you need to commit the changes
# mvn versions:commit -Pjdk-8,jdk-17