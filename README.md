# Project-v2

This monorepo contains multiple Node.js microservices built with TypeScript, designed to work together as a comprehensive platform.

## Project Structure

This project uses npm workspaces to manage multiple packages within a single repository. Each service is developed and deployed independently but shares common configuration and development practices.

### Microservices

The monorepo consists of the following microservices:

- `billing` - Handles payment processing, invoicing, subscription management, and financial transactions
- `wallet` - Manages user wallet functionality, balance tracking, and internal currency operations
- `expert-ranking` - Implements algorithms for ranking experts based on various metrics, handles search and recommendation logic
- `aicore` - Core AI functionality service that powers automated features across the platform
- `profiles` - User profile management, including profile data storage, retrieval, and updates
- `bookings` - Appointment scheduling, session reservation, calendar management, and availability tracking
- `feedback` - User reviews, ratings collection, sentiment analysis, and quality metrics
- `posts` - Content creation, storage, and delivery for user-generated content
- `authentication` - User registration, login, session management, and security features
- `call-server` - Video/audio call handling, real-time communication infrastructure
- `messages` - Direct and group messaging functionality, chat history, and message delivery status
- `notifications` - Push, email, and in-app notification management and delivery

## Technical Stack

- **Runtime Environment**: Node.js (v14 or higher recommended)
- **Package Management**: npm (v7 or higher for workspaces support)
- **Language**: TypeScript for type safety and better developer experience
- **Backend Framework**: Express.js for RESTful API endpoints
- **Build Tools**: ts-node-dev for development, TypeScript compiler for production builds
- **Concurrency**: Uses concurrently package to run multiple services during development

## Prerequisites

- Node.js (v14 or higher recommended)
- npm (v7 or higher for workspaces support)
- Git for version control
- Basic knowledge of TypeScript and Express.js
- Available ports for multiple services (default starting at 3000)

## Getting Started

### Clone the repository

```bash
git clone <repository-url>
cd Project-v2
```

### Install dependencies

Install all workspace dependencies at once:

```bash
npm install
```

Or install dependencies for specific workspaces:

```bash
npm run install:workspaces
```

### Setup environment variables

Each service might require specific environment variables. Create a `.env` file in each service directory:

```bash
touch billing/.env wallet/.env expert-ranking/.env # and so on for each service
```

### Initialize directory structure

If you need to create the basic directory structure for all services at once:

```bash
mkdir -p billing/src wallet/src expert-ranking/src aicore/src profiles/src bookings/src feedback/src posts/src authentication/src call-server/src messages/src notifications/src
```

### Development

To start all services in development mode:

```bash
npm run start:dev
```

To start a specific service (for example, billing):

```bash
npm run start:dev:billing
```

## Service Port Configuration

To avoid port conflicts, it's recommended to configure each service to use a different port:

- billing: 3001
- wallet: 3002
- expert-ranking: 3003
- aicore: 3004
- profiles: 3005
- bookings: 3006
- feedback: 3007
- posts: 3008
- authentication: 3009
- call-server: 3010
- messages: 3011
- notifications: 3012

Configure these in each service's environment variables or directly in the code.

## Workspace Setup Example (for new services)

Here's a detailed example of how to set up a new workspace with Express and TypeScript:

1. Create the workspace directory structure:

```bash
mkdir -p <workspace-name>/src/controllers <workspace-name>/src/models <workspace-name>/src/routes <workspace-name>/src/middleware <workspace-name>/src/utils
```

2. Initialize the workspace:

```bash
cd <workspace-name>
npm init -y
```

3. Update the package.json with appropriate scripts and metadata:

```json
{
  "name": "<workspace-name>",
  "version": "0.0.1",
  "description": "<workspace-name> microservice for Project-v2",
  "main": "dist/index.js",
  "scripts": {
    "build": "tsc",
    "start": "node dist/index.js",
    "start:dev": "ts-node-dev --respawn --transpile-only src/index.ts",
    "lint": "eslint . --ext .ts",
    "test": "jest"
  }
}
```

4. Install required dependencies:

```bash
npm install express cors helmet dotenv
npm install --save-dev typescript ts-node-dev @types/node @types/express @types/cors eslint @typescript-eslint/parser @typescript-eslint/eslint-plugin jest ts-jest @types/jest
```

5. Create a tsconfig.json file:

```json
{
  "compilerOptions": {
    "target": "es2018",
    "module": "commonjs",
    "outDir": "./dist",
    "rootDir": "./src",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true,
    "removeComments": true,
    "sourceMap": true,
    "declaration": true,
    "resolveJsonModule": true
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules", "**/*.test.ts", "dist"]
}
```

6. Create a basic Express app in src/index.ts:

```typescript
import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import dotenv from 'dotenv';

// Load environment variables
dotenv.config();

// Initialize express
const app = express();
const port = process.env.PORT || 3000;

// Middleware
app.use(helmet()); // Security headers
app.use(cors()); // Enable CORS
app.use(express.json()); // Parse JSON bodies
app.use(express.urlencoded({ extended: true })); // Parse URL-encoded bodies

// Routes
app.get('/health', (req, res) => {
  res.status(200).json({ status: 'ok', service: '<workspace-name>' });
});

// Basic error handling
app.use((err: any, req: express.Request, res: express.Response, next: express.NextFunction) => {
  console.error(err.stack);
  res.status(500).json({ error: 'Something went wrong!' });
});

// Start server
app.listen(port, () => {
  console.log(`<workspace-name> service is running on port ${port}`);
});

export default app;
```

7. Create a basic route structure (e.g., src/routes/index.ts):

```typescript
import express from 'express';

const router = express.Router();

router.get('/', (req, res) => {
  res.json({ message: '<workspace-name> service is running!' });
});

export default router;
```

8. Create a .gitignore file:

```
# Dependencies
node_modules/

# Build output
dist/

# Environment variables
.env

# Logs
logs/
*.log
npm-debug.log*

# Coverage directory
coverage/

# IDE files
.idea/
.vscode/
*.swp
*.swo

# OS files
.DS_Store
Thumbs.db
```

## Managing Dependencies

### Shared Dependencies

For dependencies that are common across services, consider adding them to the root package.json. This helps maintain consistent versions across the monorepo.

### Workspace-Specific Dependencies

For dependencies that are only needed in specific workspaces, install them within the workspace:

```bash
npm install <package-name> --workspace=<workspace-name>
```

## Available Scripts

- `npm start` - Starts all services in development mode
- `npm run start:dev` - Starts all services with hot-reload enabled
- `npm run start:dev:<service-name>` - Starts a specific service with hot-reload
- `npm run install:workspaces` - Installs dependencies for all workspaces
- `npm test` - Runs tests (currently not configured)

## Adding a New Workspace

1. Create the workspace directory and initial files following the structure above
2. Add the workspace to the `workspaces` array in the root package.json:
   ```json
   "workspaces": [
     "existing-workspaces",
     "new-workspace-name"
   ]
   ```
3. Add the workspace to the start scripts in the root package.json:
   ```json
   "start:dev": "concurrently \"npm run start:dev:existing-workspaces\" \"npm run start:dev:new-workspace-name\""
   ```
4. Add the workspace to the install script in the root package.json:
   ```json
   "install:workspaces": "npm install --workspace=existing-workspaces && npm install --workspace=new-workspace-name"
   ```
5. Run `npm install` to update the workspace configuration

## Communication Between Services

Services can communicate with each other using:

1. **HTTP/REST** - Direct API calls between services
2. **Message Queue** - Consider implementing RabbitMQ or Kafka for asynchronous communication

## Deployment

Each microservice can be deployed independently:

1. Build the TypeScript code:
   ```bash
   npm run build --workspace=<workspace-name>
   ```

2. Deploy the compiled JavaScript from the `dist` directory

3. Set the appropriate environment variables in your deployment environment

## Monitoring and Logging

Consider implementing a centralized logging system like ELK Stack or using a service like DataDog for production deployments.

## Version Control Strategy

- Use feature branches for development
- Merge to main branch after code review
- Consider using conventional commits for clear versioning
- Use semantic versioning for releases
- Tag releases in Git for easy tracking
- Use GitHub Actions or similar CI/CD tools for automated testing and deployment
- Ensure all tests pass before merging to main branch
