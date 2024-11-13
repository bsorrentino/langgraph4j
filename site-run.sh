#!/bin/bash

# Build sites and stage results
mvn -Pjdk-8 site:site site:stage
# Run site staging 
mvn -B -Pjdk-8 site:run -DworkingDirectory=target/staging