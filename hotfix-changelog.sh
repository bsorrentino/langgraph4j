#!/bin/bash

# prepare the CHANGELOG.md with the current version
git flow hotfix start changelog

# generate CHANGELOG.md 
git-changelog-command-line -of CHANGELOG.md

git commit -m'docs: update changelog' -a

# finish the hotfix without create tag
git flow hotfix finish changelog -n -m"changelog hotfix merge"
