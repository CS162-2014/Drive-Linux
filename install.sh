#!/bin/bash
#Alec Snyder
#install script for google drive
if [[ ! -e /lib/gdrive ]]
then
    sudo mkdir /lib/gdrive
fi

if [[ -e /lib/gdrive ]]
then
    sudo rm -rf /lib/gdrive/*
fi

if [[ ! -e ~/gdrive ]]
then
    mkdir ~/gdrive
fi
sudo cp *.java /lib/gdrive
sudo cp gdrived.c /lib/gdrive
sudo cp gdrive /bin/gdrive
sudo cp gdrive-libs.zip /lib/gdrive
sudo cp EasyReader.class /lib/gdrive
sudo cp EasyWriter.class /lib/gdrive
cp driveXml.xml ~/gdrive/.drive.xml
cd /lib/gdrive
sudo gcc gdrived.c -o gdrived
sudo mv gdrived /bin/gdrived
sudo unzip /lib/gdrive/gdrive-libs.zip
sudo javac -cp ".:./*:/lib/gdrive/*:/lib/gdrive/drive/*:/lib/gdrive/drive/libs/*" *.java
sudo chmod +x /bin/gdrive
