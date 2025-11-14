#!/bin/bash

# List of directories where npm init should be run
directories=(
  "messages"
  "posts"
  "bookings"
  "feedback"
  "call-server"
  "aicore"
  "wallet"
  "profiles"
  "authentication"
  "notifications"
  "expert-ranking"
  "billing"
)

# Loop through each directory
for dir in "${directories[@]}"; do
  echo "Checking directory $dir..."

  # Check if the directory exists, if not create it
  if [ ! -d "$dir" ]; then
    echo "Directory $dir does not exist. Creating directory..."
    mkdir -p "$dir"
  fi

  # Check if package.json already exists in the directory
  if [ -f "$dir/package.json" ]; then
    echo "package.json already exists in $dir. Skipping npm init."
  else
    echo "Initializing npm in $dir..."
    (cd "$dir" && npm init -y)
  fi
done

echo "npm init -y process completed in all directories."

