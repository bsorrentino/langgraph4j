#!/bin/bash

# prepare the CHANGELOG.md with the current version
git flow hotfix start changeme

# generate CHANGELOG.md 
git-changelog-command-line -of CHANGELOG.md

git commit -m'docs: update changeme' -a

# finish the hotfix without create tag
git flow hotfix finish changeme -n -m"changelog hotfix merge"
