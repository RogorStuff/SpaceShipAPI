#!/bin/bash

printf "[down.sh] Tearing down old artifacts\n"
sudo bash down.sh

printf "\n[up.sh] Production mode. It will tear up ALL containers\n"
mvn clean install
sudo docker-compose up --build