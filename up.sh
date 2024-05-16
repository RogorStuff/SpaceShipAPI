#!/bin/bash

if [ "$#" -ne 0 ];
    then printf "Usage: $0\n"
    exit 85
fi

printf "[down.sh] Tearing down old artifacts\n"
sudo bash down.sh

printf "\n[up.sh] Production mode. It will tear up ALL containers\n"
mvn clean install
sudo docker-compose up --build