#!/bin/bash

# Build sites and stage results
mvn site:site site:stage -T1
# Run site staging 
# mvn -B site:run -DworkingDirectory=target/staging

cp -r src/site/mkdocs target/mkdocs
cp how-tos/*.ipynb target/mkdocs/how-tos
cp -r target/staging/apidocs target/mkdocs/apidocs

source .docsenv/bin/activate

mkdocs build

mkdocs serve 

