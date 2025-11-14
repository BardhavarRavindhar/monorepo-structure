# wallet Service

## Overview

Handles user wallet functionality, balance tracking, and internal currency operations

## Technical Details

- **Service Name**: wallet
- **Default Port**: 3002
- **Technology Stack**: Node.js, TypeScript, Express

## Getting Started

### Prerequisites

- Node.js (v14 or higher)
- npm (v7 or higher)

### Installation

```bash
# Install dependencies
npm install
```

### Development

```bash
# Start in development mode with hot reload
npm run start:dev

# Build the service
npm run build

# Start the built service
npm run start
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | /health  | Health check endpoint |
| GET    | /        | Root endpoint |

## Environment Variables

Create a `.env` file in the root directory with the following variables:

```
PORT=3002
NODE_ENV=development
```

## Project Structure

```
src/
├── controllers/   # Request handlers
├── models/        # Data models
├── routes/        # API routes
├── middleware/    # Express middleware
├── utils/         # Utility functions
└── index.ts       # Application entry point
```

## Testing

```bash
# Run tests
npm test
```

## Integration with Other Services

This service is part of the Experta-v2 monorepo and may interact with other services.

