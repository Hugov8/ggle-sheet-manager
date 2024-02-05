#!/bin/sh

# --batch to prevent interactive command
# --yes to assume "yes" for questions

gpg --quiet --batch --yes --decrypt --passphrase="$LARGE_SECRET_PASSPHRASE" \
--output conf/ressources/client_secret_oauth.json \
conf/ressources/client_secret_oauth.json.gpg

gpg --quiet --batch --yes --decrypt --passphrase="$LARGE_SECRET_PASSPHRASE" \
--output conf/ressources/client_secret_service.json \
conf/ressources/client_secret_service.json.gpg