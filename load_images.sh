#!/bin/bash
IMAGES=(
  "vehiclemanager_app.tar"
  "vehiclemanager_db.tar"
)

for image in "${IMAGES[@]}"; do
  echo "Loading Docker image '${image}'..."
  docker load -i "${image}"
done
