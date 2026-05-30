# Shop Simulator - REST API Inventory Management System

A modern Spring Boot REST API application for managing shop inventory with comprehensive endpoints and in-memory data storage.

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-%23ED8B00?logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring_Boot-4.0-%236DB33F?logo=springboot&logoColor=white" alt="Spring Boot 4.0"/>
  <img src="https://img.shields.io/badge/Spring_Security-%236DB33F?logo=springsecurity&logoColor=white" alt="Spring Security"/>
  <img src="https://img.shields.io/badge/Gradle-9.0-%2302303A?logo=gradle&logoColor=white" alt="Gradle 9.0"/>
  <img src="https://img.shields.io/badge/JUnit_5-%2325A162?logo=junit5&logoColor=white" alt="JUnit 5"/>
  <img src="https://img.shields.io/badge/Cucumber-%2323D96C?logo=cucumber&logoColor=white" alt="Cucumber"/>
  <img src="https://img.shields.io/badge/PITest-1.15-%23A90533" alt="PITest"/>
  <img src="https://img.shields.io/badge/JaCoCo-0.8-%23E4405F" alt="JaCoCo"/>
  <img src="https://img.shields.io/badge/SonarQube-%234E9BCD?logo=sonarqube&logoColor=white" alt="SonarQube"/>
  <img src="https://img.shields.io/badge/JMeter-%23D22128?logo=apachejmeter&logoColor=white" alt="JMeter"/>
  <br/>
  <img src="https://img.shields.io/badge/React-19-%2361DAFB?logo=react&logoColor=white" alt="React 19"/>
  <img src="https://img.shields.io/badge/Vite-6-%23646CFF?logo=vite&logoColor=white" alt="Vite 6"/>
  <img src="https://img.shields.io/badge/TypeScript-5.8-%233178C6?logo=typescript&logoColor=white" alt="TypeScript 5.8"/>
  <img src="https://img.shields.io/badge/pnpm-%23F69220?logo=pnpm&logoColor=white" alt="pnpm"/>
  <img src="https://img.shields.io/badge/Playwright-%232EAD33?logo=playwright&logoColor=white" alt="Playwright"/>
  <br/>
  <img src="https://img.shields.io/badge/Docker-%232496ED?logo=docker&logoColor=white" alt="Docker"/>
  <img src="https://img.shields.io/badge/Azure_DevOps-%230078D7?logo=azuredevops&logoColor=white" alt="Azure DevOps"/>
  <img src="https://img.shields.io/badge/Render-%2346E3B7?logo=render&logoColor=white" alt="Render"/>
</p>

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
- **HTTP Status Codes**: 200, 201, 400, 404 based on operation result

## Architecture

The application follows a clean, layered architecture:

```
┌──────────────────────────────────────────┐
│            React SPA (frontend/)         │ ← Browser (same origin)
├──────────────────────────────────────────┤
│  InventoryController  │  AuthController  │ ← REST / SPA Routing
│  (/api/products, etc.)│  (/login, /api/me)│
├──────────────────────────────────────────┤
│              SysInventory                │ ← Business Logic
├──────────────────────────────────────────┤
│       Product  │  Validator  │  Dto      │ ← Domain / DTOs
├──────────────────────────────────────────┤
│  AppConfig  │  SecurityConfig            │ ← Configuration & Security
└──────────────────────────────────────────┘
```

### Package Structure

- **`org.Main`**: Spring Boot application entry point
- **`org.controller.InventoryController`**: REST API endpoints for product/stock/alerts
- **`org.controller.AuthController`**: SPA routing (`/login`, `/dashboard`) and `/api/me`
- **`org.dto.Dto`**: Data transfer objects (request/response records)
- **`org.app.SysInventory`**: Core business logic and in-memory inventory management
- **`org.logic.Product`**: Product entity model
- **`org.logic.Validator`**: Input validation and business rules
- **`org.config.AppConfig`**: Spring Boot configuration (CORS, SysInventory bean)
- **`org.config.SecurityConfig`**: Spring Security (form login, CSRF, HTTP Basic)

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

Use this base URL for local or deployed environments:

```http
{{baseUrl}}
```

#### Register Product
```http
POST {{baseUrl}}/api/products
```

```json
{
  "code": "PROD001",
  "name": "Laptop",
  "price": 999.99,
  "quantity": 10
}
```

#### Update Stock
```http
PATCH {{baseUrl}}/api/products/PROD001/stock
```

```json
{
  "operation": "augment",
  "quantity": 5
}
```

#### Validate Stock
```http
GET {{baseUrl}}/api/products/PROD001/validate?qty=3
```

#### API Response Format
```json
{
  "success": true,
  "message": "Product registered successfully",
  "data": "..."
}
```

Error responses:
```json
{
  "success": false,
  "message": "Insufficient stock. Available: 2, required: 3",
  "data": null
}
```

## Frontend

The web UI is a **React + Vite + TypeScript** single-page application located in
`frontend/`. It is built with **pnpm** and bundled into the Spring Boot jar as
static resources (orchestrated by the `node-gradle` plugin during the Gradle
build), so the same jar/container serves both the API and the UI.

Client-side routing is handled by **React Router v7** (`frontend/src/App.tsx`):
- `/login` — login page
- `/dashboard` — inventory dashboard (list products, register, augment/reduce stock, low-stock alert banner)
- `/*` — catch-all, redirects to `/dashboard`

The Spring Boot `AuthController` forwards `/login` and `/dashboard` to
`index.html` so React Router can handle client-side navigation when pages are
loaded directly in the browser.

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

### CSRF Protection
CSRF is **enabled** (Spring Security default). The React SPA uses the
CookieCsrfTokenRepository pattern:

1. Any **GET** request returns an `XSRF-TOKEN` cookie (non-HttpOnly, readable
   by JavaScript).
2. All mutating requests (POST, PATCH, DELETE) must echo that token in the
   `X-XSRF-TOKEN` request header.

The `JSESSIONID` cookie remains HttpOnly (not readable by JS). This is the
[official Spring recommended pattern](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa)
for SPAs.

> **For API clients (Postman, curl):** send a GET to `/login` first to obtain
> `XSRF-TOKEN`, then include `X-XSRF-TOKEN: <value>` in every mutating request.

### CORS Configuration

Cross-Origin Resource Sharing is configured globally in `AppConfig.java`:
- **Allowed origin**: `http://localhost:8080` (same-server only)
- **Allowed methods**: `GET`, `POST`, `PATCH`, `DELETE`
- **Path pattern**: `/api/**`

This restrictive policy works because:
- **Production** — the React SPA is served from the same Spring Boot server (same origin), so CORS is never triggered.
- **Development** — the Vite dev server proxies `/api` requests to `:8080` (configured in `vite.config.ts`), bypassing CORS at the browser level.

> Direct API access from a different origin will be blocked. Adjust
> `allowedOrigins` in `AppConfig.java` for your deployment needs.

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

Includes `AuthControllerTest` (unit test for SPA routing and `/api/me`
endpoint, no `@SpringBootTest` overhead) and standard domain tests.

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

By default Firefox runs headless. To watch the browser during execution (e.g.
when running from your IDE locally), disable headless mode:
```bash
./gradlew e2eTest -De2e.headless=false
```

**E2E `data-testid` attributes** (for writing additional Playwright tests):

| Attribute | Element |
|---|---|
| `data-testid="login-error"` | Login error message |
| `data-testid="username-input"` | Username field |
| `data-testid="password-input"` | Password field |
| `data-testid="login-btn"` | Login button |
| `data-testid="logout-btn"` | Logout button |
| `data-testid="dashboard-title"` | Dashboard heading |
| `data-testid="product-table"` | Product table |
| `data-testid="code-input"` | Register product code field |
| `data-testid="name-input"` | Register product name field |
| `data-testid="price-input"` | Register product price field |
| `data-testid="quantity-input"` | Register product quantity field |
| `data-testid="register-btn"` | Register product button |
| `data-testid="message"` | Dashboard feedback message |
| `data-testid="low-stock-alert"` | Low-stock alert banner |
| `data-testid="amount-{code}"` | Stock quantity input for product `{code}` |
| `data-testid="augment-{code}"` | Augment button for product `{code}` |
| `data-testid="reduce-{code}"` | Reduce button for product `{code}` |

**Mutation Testing (PITest):**
```bash
./gradlew pitest
```
PITest targets the `org.app.*` and `org.logic.*` packages (configured in `build.gradle`).

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
jmeter -n -t src/test/jmeter/tests/stress_test_pipeline.jmx -l tests/results/results.jtl -e -o tests/results/html
```
Reports generated in `ShopSimulator/results/html/`

### Code Quality

**SonarQube analysis:**
```bash
./gradlew sonar
```

Requires SonarQube server running (configured in `build.gradle`).

## Docker

The project ships a multi-stage `Dockerfile`: the builder stage (`gradle:8.7-jdk17`)
compiles the React frontend (pnpm) and the Spring Boot fat jar (`bootJar`); the
runtime stage (`eclipse-temurin:17-jre-alpine`) is a slim JRE image (~300 MB)
running as a non-root user.

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
│   │   │   ├── controller/
│   │   │   │   ├── InventoryControllerTest.java
│   │   │   │   └── AuthControllerTest.java      (SPA routing + /api/me unit tests)
│   │   │   └── logic/
│   │   │       ├── ProductTest.java
│   │   │       └── ValidatorTest.java
│   │   ├── stepdefinitions/      (Cucumber step definitions)
│   │   ├── runners/              (CucumberTestRunner — acceptance)
│   │   └── e2e/                  (Playwright E2E: runner, pages, steps, features)
│       ├── resources/
│       │   ├── junit-platform.properties  (Cucumber glue, tag filters, plugins)
│       │   └── features/                  (Cucumber .feature files; e2e/ tagged @e2e)
│       └── jmeter/
│           └── tests/                (JMeter performance test plans)
│               └── stress_test_pipeline.jmx
├── frontend/                         (React + Vite + TypeScript SPA, built with pnpm)
│   ├── src/
│   │   ├── main.tsx                  (React entry point)
│   │   ├── App.tsx                   (React Router: /login, /dashboard, /*)
│   │   ├── api.ts                    (API client with CSRF handling)
│   │   ├── types.ts                  (TypeScript interfaces: Product, ApiResponse)
│   │   ├── styles.css                (Application styles)
│   │   └── pages/
│   │       ├── Login.tsx
│   │       └── Dashboard.tsx
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

### Maven Publishing

The project publishes to an Azure DevOps Maven feed (configured in `build.gradle`):
```bash
./gradlew publish
```
Requires Azure DevOps credentials in `~/.gradle/gradle.properties`.

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

## Roadmap

### Persistent Storage
- [ ] Replace in-memory storage (`SysInventory` with `ArrayList`) with **PostgreSQL** via Spring Data JPA
- [ ] Add Flyway or Liquibase for schema migrations
- [ ] Keep in-memory fallback for tests

### SecurityConfig Tests
- [ ] Add integration tests for `SecurityConfig`:
  - CSRF cookie issuance on GET requests
  - Login/logout success (200) and failure (401)
  - Unauthenticated `/api/**` requests return 401
  - Authenticated access to protected endpoints
- [ ] Use `@SpringBootTest` + `TestRestTemplate` or `MockMvc` via `@WebMvcTest`

### Frontend Mutation Testing
- [ ] Evaluate **Stryker Mutator** for TypeScript/React (integrates with Vitest)
- [ ] Weigh CI time cost vs. benefit (PITest already adds minutes to the pipeline)
- [ ] Alternative: increase coverage thresholds for Vitest if mutation testing proves too heavy

### Additional Improvements
- [ ] Clean up dead code: `ValidateRequest` DTO in `Dto.java` is defined but never used by any endpoint
- [ ] Migrate CORS allowed origins to `application.properties` (currently hardcoded in `AppConfig.java`)
- [ ] Add OpenAPI/Swagger documentation via `springdoc-openapi`
- [ ] Introduce Spring profiles for dev/test/prod environments

## Support

For issues, questions, or contributions, please refer to the project's issue tracker or contact the development team.
