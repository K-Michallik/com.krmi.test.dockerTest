#!/bin/bash

TARGET="target"
mkdir -p ${TARGET}
mkdir -p ${TARGET}/temp
mkdir -p ${TARGET}/temp/daemon-py
mkdir -p ${TARGET}/temp/polyscope-ui

cp -r metadata.yaml ${TARGET}/temp
cp -r LICENSE ${TARGET}/temp
cp -r polyscope-ui/target/polyscope-ui-1.0-SNAPSHOT.jar ${TARGET}/temp/polyscope-ui


docker build -f ./daemon-py/Dockerfile -t modbus-docker:latest  ./daemon-py
docker save -o ${TARGET}/temp/daemon-py/daemon-py.tar modbus-docker:latest

cd ${TARGET}/temp && zip -r ../modbusDocker-1.0-SNAPSHOT.urcapx *  -x "*/.*" && cd -

rm -rf ${TARGET}/temp
