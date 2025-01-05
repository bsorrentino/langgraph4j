#!/bin/bash

# Build sites and stage results
mvn site:site site:stage
# Run site staging 
mvn -B site:run -DworkingDirectory=target/staging