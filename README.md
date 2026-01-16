# Atlas Backend Application

## Overview

Atlas is a robust backend application designed for project management and team collaboration. Built with **Spring Boot 3** and **Java 21**, it provides a comprehensive set of APIs for managing workspaces, users, tasks, and comments.

## 🚀 Tech Stack

- **Core:** Java 21, Spring Boot 3.2.2
- **Database:** PostgreSQL
- **Caching:** Redis
- **Security:** Spring Security, JWT (JSON Web Tokens)
- **Database Migration:** Flyway
- **API Documentation:** Swagger / OpenAPI 3
- **Containerization:** Docker & Docker Compose
- **Tooling:** Maven, Lombok

## 📂 Project Structure

The application is structured around domain-driven modules:

- **Auth:** Authentication and authorization logic.
- **Users:** User profile management.
- **Workspace:** Workspace creation and configuration.
- **WorkspaceMembers:** Member management within workspaces.
- **Task:** Task creation, assignment, and tracking.
- **Comments:** Commenting system for tasks.
- **Config:** Application-wide configurations (Security, Redis, OpenAPI).

## 🛠 Prerequisites

Ensure you have the following installed:

- **Java 21 JDK**
- **Maven**
- **Docker & Docker Compose**

## ⚙️ Configuration

The application uses **Spring Profiles** to manage configuration.

### Environment Variables

The application relies on the following environment variables for database connection (defined in `src/main/resources/application.yaml`). You must set these when running the application locally:

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_URL` | JDBC URL for PostgreSQL | `jdbc:postgresql://localhost:5433/atlas` |
| `DB_NAME` | Database Username | `user` |
| `DB_PASSWORD` | Database Password | `password` |

### Redis Configuration
By default, the application expects Redis at `localhost:6379`. If you use the provided `docker-compose.yml` for dependencies, Redis is mapped to port **6380**. You may need to override `spring.data.redis.port` in your configuration or update your run configuration.

## 🏃‍♂️ Getting Started

### 1. Start Infrastructure Services

Use Docker Compose to start PostgreSQL and Redis:

```bash
docker-compose up db redis -d
```

> **Note:**
> - PostgreSQL will be available at `localhost:5433`
> - Redis will be available at `localhost:6380`

### 2. Build the Application

```bash
mvn clean install
```

### 3. Run Locally

You can run the application using Maven or your IDE. Ensure you provide the necessary environment variables.

**Using CLI (PowerShell example):**

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5433/atlas"
$env:DB_NAME="user"
$env:DB_PASSWORD="password"
# Override Redis port since docker-compose uses 6380
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.data.redis.port=6380"
```

**Using IDE(IntelliJ/Eclipse):**
Set the environment variables in your Run Configuration and add `--spring.data.redis.port=6380` to your Program Arguments.

### 4. Run Fully with Docker

To run the entire stack (Database, Redis, and Application) within Docker:

```bash
docker-compose up --build
```

The application will be accessible at `http://localhost:8080`.

## 📚 API Documentation

Once the application is running, you can explore and test the APIs using the Swagger UI:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

## 🛡 Security

The application uses JWT for security.
1.  **Sign Up/Login:** Use the Auth endpoints (`/auth/**`) to get a token.
2.  **Authentication:** Pass the token in the `Authorization` header as `Bearer <token>` for protected endpoints.

## 🧪 Testing

Run unit and integration tests using Maven:

```bash
mvn test
```
