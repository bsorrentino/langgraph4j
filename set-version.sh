#!/bin/bash

read -p "verion: " ver

mvn versions:set -DnewVersion=$ver -Pjdk-8,jdk-17

