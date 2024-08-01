#!/bin/bash

#
# Prepare GPG Key is expected to be in base64
# Exported with gpg -a --export-secret-keys "your@email" | base64 > gpg.base64
#
printf "$GPG_KEY_BASE64" | base64 --decode > gpg.asc
echo ${GPG_PASSPHRASE} | gpg --batch --yes --passphrase-fd 0 --import gpg.asc
gpg -k