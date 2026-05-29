# Shop Simulator - REST API Inventory Management System

A modern Spring Boot REST API application for managing shop inventory with comprehensive endpoints and in-memory data storage.

## Overview

Shop Simulator is a backend inventory management system designed for small to medium-sized retail businesses. 
It provides RESTful API endpoints for managing products, tracking stock levels, and maintaining inventory data in memory.

## Features

### Core Functionality
- **Product Management**: Register, update, and track products with unique codes
- **Stock Control**: Increase or decrease product quantities with validation
- **Inventory Validation**: Check stock availability for required quantities
- **Low Stock Alerts**: Automatic alerts when products fall below threshold (5 units)
- **In-Memory Storage**: Data is stored in application memory during runtime

### API Features
- **REST API**: Full CRUD operations via HTTP endpoints
- **Statistics Endpoint**: Inventory metrics (total products, low stock count, total value)
- **Low Stock Alerts**: Automatic detection and endpoint to query alerts
- **JSON API**: Structured request/response format
- **HTTP Status Codes**: 200, 201, 400, 404 based on operation result

## Architecture

The application follows a clean, layered architecture:

```
┌─────────────────┐
│ InventoryController │ ← REST API Layer
├─────────────────┤
│   SysInventory  │ ← Business Logic Layer
├─────────────────┤
│ Product/Validator│ ← Domain Models
└─────────────────┘
```

### Package Structure

- **`org.Main`**: Spring Boot application entry point
- **`org.controller.InventoryController`**: REST API endpoints
- **`org.dto.Dto`**: Data transfer objects for API requests/responses
- **`org.app.SysInventory`**: Core business logic and inventory management
- **`org.logic.Product`**: Product entity model
- **`org.logic.Validator`**: Input validation and business rules
- **`org.config.AppConfig`**: Spring Boot configuration class

## Installation & Setup

### Prerequisites
- Java 17 or higher
- The Gradle Wrapper provides Gradle 9.0 (no local Gradle install required)
- Node.js and pnpm are downloaded automatically by the build (node-gradle plugin) to compile the React frontend
- Docker (optional, for containerized run/deploy)

### Build Instructions

1. Clone the repository:
```bash
git clone <repository-url>
cd ShopSimulator
```

2. Build the project:
```bash
./gradlew build
```

3. Run the application:
```bash
./gradlew bootRun
```

4. The API will be available at:
```
http://localhost:8080/api
```

### Dependencies

- **Spring Boot**: Web framework with embedded Tomcat server
- **Spring Security**: Form login + HTTP Basic authentication
- **React + Vite + TypeScript**: Single-page frontend (built with pnpm)
- **Jackson**: JSON serialization/deserialization
- **JUnit 5**: Unit testing framework
- **Cucumber**: Acceptance testing (BDD)
- **Playwright (Firefox)**: End-to-end testing
- **PITest**: Mutation testing
- **JaCoCo**: Code coverage reporting
- **SonarQube**: Code quality analysis
- **JMeter**: Performance/load testing
- **Gradle**: Build automation
- **Docker**: Containerization (multi-stage build)

## API Endpoints

### Product Management
- `GET /api/products` - List all products
- `POST /api/products` - Register new product
- `GET /api/products/{code}` - Get product by code
- `PATCH /api/products/{code}/stock` - Update stock quantity
- `GET /api/products/{code}/validate` - Validate stock availability

### Statistics & Alerts
- `GET /api/stats` - Get inventory statistics (total products, low stock count, total value, total alerts)
- `GET /api/alerts` - Get low stock alerts
- `DELETE /api/alerts` - Clear all alerts

### Request/Response Examples

#### Register Product
```json
POST /api/products
{
  "code": "PROD001",
  "name": "Laptop",
  "price": 999.99,
  "quantity": 10
}
```

#### Update Stock
```json
PATCH /api/products/PROD001/stock
{
  "operation": "augment",
  "quantity": 5
}
```

#### API Response Format
```json
{
  "success": true,
  "message": "Product registered successfully",
  "data": { ... }
}
```

## Frontend

The web UI is a **React + Vite + TypeScript** single-page application located in
`frontend/`. It is built with **pnpm** and bundled into the Spring Boot jar as
static resources (orchestrated by the `node-gradle` plugin during the Gradle
build), so the same jar/container serves both the API and the UI.

- `/login` — login page
- `/dashboard` — inventory dashboard (list + register products, logout)

**Frontend development server** (hot reload, proxies `/api` to `:8080`):
```bash
cd frontend
pnpm install
pnpm dev        # http://localhost:5173
```

## Authentication

The application is protected with **Spring Security**:
- Login page at `/login`; the SPA authenticates against `POST /api/login`
  (form login). HTTP Basic is also accepted (used by JMeter).
- All `/api/**` endpoints (except `/api/login`) require authentication and
  return `401` otherwise.
- Default credentials: `admin` / `admin123`.
- Credentials are overridable via environment variables
  `APP_ADMIN_USERNAME` / `APP_ADMIN_PASSWORD` (used in cloud deployment).

### Auth endpoints
- `POST /api/login` — form login (`200` on success, `401` on failure)
- `POST /api/logout` — ends the session (`200`)
- `GET /api/me` — returns the current authenticated username

## Data Storage

**In-Memory Only**: Data is stored exclusively in memory during execution. 
When the application restarts, all data is lost and the inventory starts empty.

## Validation Rules

### Product Validation
- **Code**: Alphanumeric characters only, required
- **Name**: Minimum 3 characters, required
- **Price**: Must be greater than 0
- **Quantity**: Must be non-negative

### Business Rules
- Product codes must be unique
- Stock reduction requires sufficient inventory
- Low stock threshold: 5 units
- Operation quantities must be positive

## Error Handling

The application provides comprehensive error handling:
- Input validation with descriptive error messages
- HTTP 400/404 responses for invalid operations
- Detailed logging for debugging
- Validation of business rules (insufficient stock, duplicate product, etc.)

## Development

### Running Tests

**Unit Tests:**
```bash
./gradlew test
```

**Acceptance Tests (Cucumber BDD):**
```bash
./gradlew acceptanceTest
```

**E2E Tests (Playwright + Firefox):**
```bash
# Terminal 1: start the app
./gradlew bootRun

# Terminal 2: run the E2E suite (Firefox auto-downloads on first run)
./gradlew e2eTest
```
E2E scenarios are tagged `@e2e` and are intentionally excluded from `test` and
`acceptanceTest` so CI does not require a live app or a browser.

**Mutation Testing (PITest):**
```bash
./gradlew pitest
```

**Code Coverage (JaCoCo):**
```bash
./gradlew jacocoTestReport
```
Reports generated in `build/reports/jacoco/test/html/`

**Performance Testing (JMeter):**
Load test plans located in `src/test/jmeter/tests/`
- `stress_test_pipeline.jmx` - Stress test configuration

Run with JMeter GUI or CLI:
```bash
jmeter -n -t tests/stress_test_pipeline.jmx -l tests/results/results.jtl -e -o tests/results/html
```
Reports generated in `ShopSimulator/results/html/`

### Code Quality

**SonarQube analysis:**
```bash
./gradlew sonar
```

Requires SonarQube server running (configured in `build.gradle`).

## Docker

The project ships a multi-stage `Dockerfile`: the builder stage compiles the
React frontend (pnpm) and the Spring Boot fat jar (`bootJar`); the runtime stage
is a slim JRE image (~300 MB) running as a non-root user.

```bash
# Build image
docker build -t shopsimulator:latest .

# Run container
docker run -d -p 8080:8080 --name shopsimulator shopsimulator:latest

# App available at http://localhost:8080/login
```

The container honors a `PORT` environment variable (defaults to `8080`) and
shuts down gracefully on `SIGTERM`. Admin credentials can be injected via
`APP_ADMIN_USERNAME` / `APP_ADMIN_PASSWORD`.

## Cloud Deployment

The application is deployed on **Render** as a Docker web service, configured
via `render.yaml` (Blueprint):
- Auto-deployed from the `main` branch.
- Render injects `PORT`; the container binds to it automatically.
- `APP_ADMIN_PASSWORD` is set in the Render dashboard (`sync: false`, never
  committed).
- Health check path: `/login`.

```bash
# Verify the deployed service
curl -L https://<your-domain>.onrender.com/login
```

The Azure DevOps pipeline (`azure-pipelines.yml`) remains the quality/CI
pipeline and is independent of cloud hosting.

### Project Structure
```
ShopSimulator/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/
│   │   │       ├── Main.java
│   │   │       ├── controller/
│   │   │       │   ├── InventoryController.java
│   │   │       │   └── AuthController.java       (SPA routing + /api/me)
│   │   │       ├── dto/
│   │   │       │   └── Dto.java
│   │   │       ├── app/
│   │   │       │   └── SysInventory.java
│   │   │       ├── logic/
│   │   │       │   ├── Product.java
│   │   │       │   └── Validator.java
│   │   │       └── config/
│   │   │           ├── AppConfig.java
│   │   │           └── SecurityConfig.java       (Spring Security)
│   └── test/
│       ├── java/
│       │   ├── org/
│       │   │   ├── app/
│       │   │   │   └── SysInventoryTest.java
│       │   │   ├── controller/
│       │   │   │   └── InventoryControllerTest.java
│       │   │   └── logic/
│       │   │       ├── ProductTest.java
│       │   │       └── ValidatorTest.java
│       │   ├── stepdefinitions/      (Cucumber step definitions)
│       │   ├── runners/              (CucumberTestRunner — acceptance)
│       │   └── e2e/                  (Playwright E2E: runner, pages, steps)
│       ├── resources/
│       │   └── features/             (Cucumber .feature files; e2e/ tagged @e2e)
│       └── jmeter/
│           └── tests/                (JMeter performance test plans)
│               └── stress_test_pipeline.jmx
├── frontend/                         (React + Vite + TypeScript SPA, built with pnpm)
│   ├── src/
│   ├── index.html
│   ├── package.json
│   ├── pnpm-lock.yaml
│   ├── tsconfig.json
│   └── vite.config.ts
├── Dockerfile                        (multi-stage build)
├── .dockerignore
├── render.yaml                       (Render deployment blueprint)
├── .gitattributes
├── .gitignore
├── azure-pipelines.yml
├── build.gradle
├── gradlew
├── gradlew.bat
├── README.md
└── settings.gradle
```

## Configuration

### Low Stock Threshold
Modify `MINIMUM_STOCK_ALERT` in `Validator.java` to change the alert threshold:
```java
public static final int MINIMUM_STOCK_ALERT = 5; // Default: 5 units
```

### Server Port
Default port is 8080. Override with:
```bash
./gradlew bootRun --args="--server.port=8081"
```

## Contributing

1. Follow Java naming conventions
2. Add appropriate validation for new features
3. Include comprehensive error handling
4. Write unit tests for new functionality
5. Update documentation for API changes

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Version History

- **v1.0-SNAPSHOT**: Spring Boot REST API
  - Complete REST API endpoints for products, stock, and alerts
  - In-memory storage 
  - Unit tests with JUnit 5
  - Acceptance tests with Cucumber (BDD)
  - Mutation testing with PITest
  - Code coverage with JaCoCo
  - SonarQube integration
  - Quantity validation and business rules
  - Authentication with Spring Security (form login + HTTP Basic)
  - React + Vite + TypeScript frontend bundled into the jar
  - End-to-end tests with Playwright (Firefox)
  - Multi-stage Docker image
  - Cloud deployment on Render (`render.yaml`)

## Documentation

- [Critical Path](docs/critical-path.md) — essential functionality, dependencies and risk matrix
- [Test Plan](docs/test-plan.md) — testing strategy, suites and test cases

## Code Quality & Analysis

This section documents the code quality analysis and technical debt tracking throughout the project development.

### Initial Analysis
The following image shows the first analysis performed on the initial codebase:

![First Analysis](assets/images/firstanalysis.png)

### Initial Technical Debt
The initial technical debt identified due to bad practices in the early development:

![First Technical Debt](assets/images/firsttechnicaldebt.png)

### Mutation Testing
Mutation testing performed with PITest after project updates and improvements:

![Mutation Test](assets/images/mutationtest.png)

### Updated SonarQube Analysis
Recent SonarQube scan showing the current state of code quality:

![Updated SonarQube Analysis](assets/images/sonarqupdated.png)

### Current Technical Debt
The current technical debt status after all improvements and refactoring:

![Current Technical Debt](assets/images/newtechnicaldebt.png)

## Support

For issues, questions, or contributions, please refer to the project's issue tracker or contact the development team.
