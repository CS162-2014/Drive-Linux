#!/bin/bash
#Alec Snyder
#uninstall gdrive

if [[ -d /lib/gdrive ]]
then
    sudo rm -rf /lib/gdrive
fi
sudo rm /bin/gdrive
sudo rm /bin/gdrived
sudo rm /usr/share/applications/google-drive.desktop

