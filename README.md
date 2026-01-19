# Atlas Backend

Atlas is a robust project management and collaboration backend built with modern Java standards. It serves as the core API for managing workspaces, tasks, comments, and team collaboration.

## 🚀 Technology Stack

- **Java:** 21+ (Configured for Java 25)
- **Framework:** Spring Boot 4.0.1
- **Database:** PostgreSQL
- **Caching:** Redis
- **Security:** Spring Security & JWT
- **Migration:** Flyway
- **Documentation:** OpenAPI 3 (Swagger)
- **Containerization:** Docker & Docker Compose
- **Resilience:** Custom Rate Limiting

## 📂 Project Structure

The application follows a domain-driven package structure under `com.example.atlas`:

| Module | Description |
|--------|-------------|
| `auth` | Authentication logic & token management |
| `users` | User profile & account management |
| `workspace` | Workspace creation & settings |
| `workspacemembers` | Member roles & permissions |
| `task` | Task management & tracking |
| `comments` | Task commenting system |
| `filter` | Security filters (JWT, Rate Limiting) |
| `refreshToken` | JWT refresh token handling |
| `config` | App-wide configuration (Security, Redis, etc.) |
| `exception` | Global exception handling |

## 🛠 Prerequisites

- **Java JDK 21** or higher
- **Maven**
- **Docker** & **Docker Compose**

## ⚙️ Configuration

The application uses `application.yaml` for configuration. Key environment variables:

| Variable | Description | Default (Docker) | Local Example |
|----------|-------------|------------------|---------------|
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://db:5432/atlas` | `jdbc:postgresql://localhost:5432/atlas` |
| `DB_NAME` | Database User | `user` | `user` |
| `DB_PASSWORD` | Database Password | `password` | `password` |
| `spring.data.redis.host` | Redis Host | `redis` | `localhost` |

## 🏃‍♂️ Getting Started

### Option 1: Run with Docker (Recommended)

Since the Dockerfile uses a pre-built JAR, you must package the application first:

1. **Build the JAR file:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Run the application stack:**
   ```bash
   docker-compose up --build
   ```
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Option 2: Local Development

1. **Start dependencies** (PostgreSQL & Redis):
   ```bash
   docker-compose up db redis -d
   ```
   *Note: This maps Postgres to port `5432` and Redis to `6379` on localhost.*

2. **Run the application**:
   
   **Using Maven:**
   ```bash
   # Linux/Mac
   export DB_URL=jdbc:postgresql://localhost:5432/atlas
   export DB_NAME=user
   export DB_PASSWORD=password
   mvn spring-boot:run

   # Windows (PowerShell)
   $env:DB_URL="jdbc:postgresql://localhost:5432/atlas"
   $env:DB_NAME="user"
   $env:DB_PASSWORD="password"
   mvn spring-boot:run
   ```

### 👤 Default Credentials

On first startup, the application creates a default Admin user:
- **Email:** `admin@gmail.com`
- **Password:** `admin123`

## 📚 API Documentation

Interactive API documentation is available via Swagger UI when the app is running:

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

## 🛡 Security

The application implements:
1. **JWT Authentication**: Secured endpoints require a valid Bearer token.
2. **Rate Limiting**: Custom filter to prevent abuse.
3. **Role-Based Access**: Granular permissions (USER, ADMIN, etc.).

## 🧪 Testing

To run unit and integration tests:

```bash
mvn test
```
