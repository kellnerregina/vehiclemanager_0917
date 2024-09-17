@echo off
set IMAGES=vehiclemanager_app.tar vehiclemanager_db.tar

for %%i in (%IMAGES%) do (
    echo Loading Docker image %%i...
    docker load -i %%i
)