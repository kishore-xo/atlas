# Atlas Backend

Atlas is a robust project management and collaboration platform backend built with **Spring Boot**. It provides a secure, scalable REST API for managing workspaces, tasks, users, and real-time chat.

## 🚀 Tech Stack

- **Core**: Java, Spring Boot
- **Database**: PostgreSQL (Primary), Redis (Caching & Pub/Sub)
- **Security**: JWT Authentication, Spring Security
- **Data Migration**: Flyway
- **Containerization**: Docker, Docker Compose

## 📂 Project Structure

The project is organized by feature:

- `auth` - Authentication & Token management
- `users` - User profile management
- `workspace` - Workspace creation and settings
- `workspacemembers` - Member management within workspaces
- `task` - Task management (CRUD, assignment)
- `comments` - Task comments
- `chat` - Real-time chat functionality

## 🛠️ Setup & Installation

### Prerequisites
- Docker & Docker Compose
- Java 21+ (Recommended)
- Maven

### Running with Docker (Recommended)

The easiest way to run the entire stack (Database, Redis, and App) is via Docker Compose.

```bash
docker-compose up --build
```

This will start:
- **Atlas Backend**: `http://localhost:8080`
- **PostgreSQL**: Port `5432`
- **Redis**: Port `6379`

### Running Locally

1. **Start Infrastructure**:
   Start only the databases using Docker:
   ```bash
   docker-compose up db redis -d
   ```

2. **Configure Environment**:
   Ensure `src/main/resources/application.properties` (or yml) points to your local DB ports.

3. **Run Application**:
   ```bash
   ./mvnw spring-boot:run
   ```

## 🧪 API Documentation

Once the application is running, you can access the API documentation (if available via SpringDoc) at:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
