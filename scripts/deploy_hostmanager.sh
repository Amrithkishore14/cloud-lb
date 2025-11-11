#!/bin/bash
set -e

echo "🧠 Starting HostManager and scaling containers..."

# Stop any existing filestore containers
echo "🧹 Cleaning up old containers..."
docker rm -f $(docker ps -aq --filter "name=filestore") 2>/dev/null || true

# Start baseline containers
echo "🪣 Creating 4 filestore containers..."
for i in {1..4}; do
    docker run -d --name "filestore-$(date +%s)$i" linuxserver/openssh-server
done

# Verify
echo "✅ Active containers:"
docker ps --filter "name=filestore"

echo "🧩 HostManager will auto-distribute chunks now."

