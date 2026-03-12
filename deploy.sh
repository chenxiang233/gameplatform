#!/bin/bash
set -e

echo "Pull latest code..."
git pull origin main

echo "Build jar..."
mvn clean package -DskipTests

echo "Build docker image..."
sudo docker build -t game-server .

echo "Restart services..."
sudo docker compose down
sudo docker compose up -d

echo "Deploy done."
