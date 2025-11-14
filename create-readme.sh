#!/bin/bash

# List of directories where README.md files should be created
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

# Function to generate README content based on service name
generate_readme_content() {
  local service_name=$1
  local service_description=""
  local service_port=""
  
  # Define specific descriptions and ports for each service
  case "$service_name" in
    "messages")
      service_description="Handles direct and group messaging functionality, chat history, and message delivery status"
      service_port="3011"
      ;;
    "posts")
      service_description="Manages content creation, storage, and delivery for user-generated content"
      service_port="3008"
      ;;
    "bookings")
      service_description="Provides appointment scheduling, session reservation, calendar management, and availability tracking"
      service_port="3006"
      ;;
    "feedback")
      service_description="Collects user reviews, ratings, performs sentiment analysis, and tracks quality metrics"
      service_port="3007"
      ;;
    "call-server")
      service_description="Manages video/audio call handling and real-time communication infrastructure"
      service_port="3010"
      ;;
    "aicore")
      service_description="Powers automated features across the platform with core AI functionality"
      service_port="3004"
      ;;
    "wallet")
      service_description="Handles user wallet functionality, balance tracking, and internal currency operations"
      service_port="3002"
      ;;
    "profiles")
      service_description="Manages user profile data storage, retrieval, and updates"
      service_port="3005"
      ;;
    "authentication")
      service_description="Provides user registration, login, session management, and security features"
      service_port="3009"
      ;;
    "notifications")
      service_description="Manages push, email, and in-app notification creation and delivery"
      service_port="3012"
      ;;
    "expert-ranking")
      service_description="Implements algorithms for ranking experts based on various metrics, handles search and recommendation logic"
      service_port="3003"
      ;;
    "billing")
      service_description="Processes payments, invoicing, subscription management, and financial transactions"
      service_port="3001"
      ;;
    *)
      service_description="Microservice for the Experta-v2 platform"
      service_port="3000"
      ;;
  esac

  # Generate README content
  cat << EOF
# ${service_name} Service

## Overview

${service_description}

## Technical Details

- **Service Name**: ${service_name}
- **Default Port**: ${service_port}
- **Technology Stack**: Node.js, TypeScript, Express

## Getting Started

### Prerequisites

- Node.js (v14 or higher)
- npm (v7 or higher)

### Installation

\`\`\`bash
# Install dependencies
npm install
\`\`\`

### Development

\`\`\`bash
# Start in development mode with hot reload
npm run start:dev

# Build the service
npm run build

# Start the built service
npm run start
\`\`\`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | /health  | Health check endpoint |
| GET    | /        | Root endpoint |

## Environment Variables

Create a \`.env\` file in the root directory with the following variables:

\`\`\`
PORT=${service_port}
NODE_ENV=development
\`\`\`

## Project Structure

\`\`\`
src/
├── controllers/   # Request handlers
├── models/        # Data models
├── routes/        # API routes
├── middleware/    # Express middleware
├── utils/         # Utility functions
└── index.ts       # Application entry point
\`\`\`

## Testing

\`\`\`bash
# Run tests
npm test
\`\`\`

## Integration with Other Services

This service is part of the Experta-v2 monorepo and may interact with other services.

EOF
}

# Loop through each directory
for dir in "${directories[@]}"; do
  echo "Checking directory $dir..."
  
  # Check if the directory exists, if not create it
  if [ ! -d "$dir" ]; then
    echo "Directory $dir does not exist. Creating directory..."
    mkdir -p "$dir"
  fi
  
  # Check if README.md already exists in the directory
  if [ -f "$dir/README.md" ]; then
    echo "README.md already exists in $dir. Do you want to overwrite it? (y/n)"
    read -r overwrite
    if [[ "$overwrite" != "y" ]]; then
      echo "Skipping README.md creation for $dir."
      continue
    fi
  fi
  
  echo "Creating README.md in $dir..."
  generate_readme_content "$dir" > "$dir/README.md"
  echo "README.md created in $dir."
done

echo "README.md creation process completed in all directories."
