# bookings Service

## Overview

Provides appointment scheduling, session reservation, calendar management, and availability tracking

## Technical Details

- **Service Name**: bookings
- **Default Port**: 3006
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
PORT=3006
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

