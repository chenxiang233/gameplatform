#!/bin/bash
set -e

echo "Pull latest code..."
git pull origin main

echo "Build jar..."
mvn clean package -DskipTests

echo "Build docker image..."
docker build -t game-server .

echo "Restart services..."
docker compose down
docker compose up -d

echo "Deploy done."
