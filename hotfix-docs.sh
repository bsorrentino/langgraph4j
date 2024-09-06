#!/bin/bash

# prepare the CHANGELOG.md with the current version
git flow hotfix start docs

read -p "commit message: " msg

git commit -m"docs: $msg" -a

# finish the hotfix without create tag
git flow hotfix finish docs -n -m"docs hotfix merge"
