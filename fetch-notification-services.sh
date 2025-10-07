#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <username> <password> <base_url>"
    exit 1
fi

USERNAME="$1"
PASSWORD="$2"
BASE_URL="$3"

BASE_URL="${BASE_URL%/}"

LOGIN_RESPONSE=$(curl -s -X POST "${BASE_URL}/api/auth/login" \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"${USERNAME}\",\"password\":\"${PASSWORD}\"}")

API_KEY=$(echo "$LOGIN_RESPONSE" | jq -r '.result.api_key')

if [ -z "$API_KEY" ] || [ "$API_KEY" == "null" ]; then
    echo "Authentication failed. Response:" >&2
    echo "$LOGIN_RESPONSE" >&2
    exit 1
fi

curl -s -X GET "${BASE_URL}/api/notificationservices?api_key=${API_KEY}" | jq "."
