# Atlas Project Audit — Current State & Action Plan ✅

**Summary (short)**
This README has been refreshed to reflect the current repository state: resolved issues were removed, remaining problems are listed by priority, and concrete fixes + test suggestions are provided. I also included quick code snippets and a prioritized action plan to remediate security and correctness issues.

---

## ✅ What I fixed already
- Added custom exceptions `NotFoundException` and `BadRequestException` and a global handler `GlobalExceptionHandling` that returns consistent `ApiError` responses for 404/400/validation/500 cases.
- Fixed `TaskService.getTasks` mapping and `TaskController#getTasks` to return `TaskResponse` DTOs.
- Implemented `WorkSpaceService` to return `WorkSpaceResponse` with task summaries (comments deliberately excluded to keep workspace payloads light).
- Updated entities to use `@CreationTimestamp` and corrected date format patterns in most places.

---

## 🔴 High priority — Security
These should be addressed immediately.

- DB credentials committed to repository
  - Location: `src/main/resources/application.yaml` (contains `username` and `password` in clear text).
  - Risk: leaked credentials allow unauthorized DB access.
  - Fix: Remove credentials from the repo and use environment variables or a secrets manager. Example:
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/atlas
        username: ${DB_USERNAME:}
        password: ${DB_PASSWORD:}
    ```
  - Action: Remove `username/password` from the file and set `DB_USERNAME`/`DB_PASSWORD` in environment/CI.

- Plain-text user passwords and API leakage
  - Files: `src/main/java/com/example/atlas/users/Users.java`, `src/main/java/com/example/atlas/users/dto/UserResponse.java`, `src/main/java/com/example/atlas/users/UserService.java`.
  - Problems:
    - `Users.password` is stored as raw text and is not annotated with `@JsonIgnore` (risk of accidental serialization).
    - `UserResponse` currently includes `password` and the service returns it in responses.
    - `UserService` stores raw passwords directly from requests.
  - Fixes:
    1. **Do not return passwords in API responses** — remove the `password` field from `UserResponse`.
    2. **Prevent accidental serialization** of `Users.password`: annotate with `@JsonIgnore`.
    3. **Hash passwords** before storing using a `PasswordEncoder` (BCrypt):
       ```java
       @Bean
       public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
       // then in service:
       users.setPassword(passwordEncoder.encode(userRequest.getPassword()));
       ```
    4. **Validate password strength** on input.

---

## 🟠 High / Medium priority — Correctness & API design

- Consistent timestamps and date formats
  - Files: `Users.java` createdAt uses `@JsonFormat(pattern = "dd-MM-yyyy HH-mm-ss")` (note `HH-mm-ss`), which is inconsistent with others (`HH:mm:ss`).
  - Fix: standardize to `@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")` across entities and use `@CreationTimestamp`.

- Package naming conventions
  - Some packages use capitalized segments (e.g., `com.example.atlas.task`, `com.example.atlas.comments`) instead of all-lowercase packages. This is a style issue and could be addressed in a maintenance refactor.

- Actuator endpoints exposure
  - File: `application.yaml` has `management.endpoints.web.exposure.include: "*"` — restrict this in production (e.g., `health,info`) and protect endpoints with authentication.

- DTO coverage / Entities leaking
  - Controllers currently use DTOs for the primary flows (Users/Tasks/Comments/Workspace). Continue converting any remaining direct entity returns to DTOs to avoid leaking internal state.

---

## 🟢 Low priority — Cleanups & tests

- Tests missing for global exception handling and validation
  - Add MockMvc tests for the `MethodArgumentNotValidException` (validation errors) and for custom exceptions (404/400) to verify `ApiError` payloads.

- Add a secrets scanning CI job
  - Add a GitHub Action or pre-commit hook to detect committed secrets (e.g., truffleHog or git-secrets) to avoid future leaks.

---

## ✅ Concrete quick fixes (copy-paste)

1) Remove password from `UserResponse`:
```java
// users/dto/UserResponse.java
public record UserResponse(Long id, String name, String email, @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime createdAt) {}
```

2) Hide password in `Users` entity:
```java
// users/Users.java
@JsonIgnore
@Column(nullable = false)
private String password;
```

3) Hash passwords in `UserService` and add a bean:
```java
@Bean
public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

// in UserService.createUser
users.setPassword(passwordEncoder.encode(userRequest.getPassword()));
```

4) Use env placeholders for DB credentials (`application.yaml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/atlas
    username: ${DB_USERNAME:}
    password: ${DB_PASSWORD:}
```

5) Fix timestamp formats:
```java
@CreationTimestamp
@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
private LocalDateTime createdAt;
```

6) Add MockMvc tests for validation and exception mapping (examples were prepared in the audit).

---

## Action Plan (priority + tasks) 🔧
1. Immediate security fixes (branch: `fix/security-and-api-cleanup`)
   - Remove `username/password` from `application.yaml` and wire env vars.
   - Remove `password` from `UserResponse`, add `@JsonIgnore` to `Users.password`, hash passwords in `UserService`, add tests.
   - Add secrets detection CI job.
2. Validation & exceptions
   - Add MockMvc tests for `MethodArgumentNotValidException` and for custom exceptions.
3. Consistency & polish
   - Fix `Users.createdAt` format, consider package naming cleanup (refactor in separate PR).

---

## Tests & verification
- Run `\\.\mvnw test` after adding tests.
- Recommended tests to add now:
  - `UserControllerValidationTest` (invalid email, short password) -> expects 400 with `ApiError`.
  - `UserControllerNotFoundTest` -> mock `UserService#getUser` to throw `NotFoundException` and assert 404 + message.
  - `TaskControllerBadRequestTest` -> mock service to throw `BadRequestException` and assert 400.

---

## Next step
I can implement the immediate security fixes (branch and PR) and add the basic tests in this repo now. Choose one:
- **A** — Implement high-priority fixes + unit/integration tests now (recommended)
- **B** — Prepare a full PR with all changes and tests
- **C** — Provide a detailed patch / step-by-step guide only

Reply with your choice and I'll begin the requested work.
