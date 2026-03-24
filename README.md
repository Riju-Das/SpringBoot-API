# Content Calendar API

Spring Boot REST API with JWT auth, refresh tokens, RBAC, pagination, Redis caching, and Dockerized setup.

## Features

- JWT access token + refresh token cookie flow
- Role-based authorization (`USER`, `ADMIN`)
- User content CRUD + filter/search + pagination
- Admin APIs for all users and all content
- Redis caching + Redis refresh token storage (TTL)
- IP rate limiting and global exception handling

## Tech Stack

- Java 17
- Spring Boot 4.0.2
- Spring Security
- Spring Data JPA (PostgreSQL)
- Spring Data Redis
- JWT (`jjwt`)
- Bucket4j
- Docker + Docker Compose
- Maven

## Architecture Overview

```text
src/main/java/com/example/content_calender
  controller/   -> API endpoints (Auth, Content, Admin)
  service/      -> Business logic layer
  security/     -> JWT utility/filter, auth service, rate limiting
  model/        -> JPA and Redis entities
  repository/   -> JPA and Redis repositories
  config/       -> Security, Redis cache, admin seeding
  exception/    -> Global API exception handling
```

## Security Design

- JWT in `Authorization: Bearer <token>` header
- Refresh token in HttpOnly cookie (`/api/auth/refresh` path)
- Route access:
  - `/api/auth/**` -> public
  - `/api/content/**` -> `USER` or `ADMIN`
  - `/api/admin/**` -> `ADMIN` only
- Refresh tokens stored in Redis with TTL
- Old tokens removed on login (`deleteByUserId`)

## API Endpoints

### Authentication Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/signup` | Public | Register a new user (`USER` role) |
| `POST` | `/api/auth/login` | Public | Authenticate user, return JWT, set refresh cookie |
| `POST` | `/api/auth/refresh` | Public (cookie required) | Issue new access token using refresh token |
| `POST` | `/api/auth/logout` | Public (cookie optional) | Delete refresh token and clear cookie |

### User Content Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/content` | USER/ADMIN | Get authenticated user's content (paginated) |
| `GET` | `/api/content/{id}` | USER/ADMIN | Get a single content item owned by user |
| `POST` | `/api/content` | USER/ADMIN | Create content for authenticated user |
| `PUT` | `/api/content/{id}` | USER/ADMIN | Update owned content |
| `DELETE` | `/api/content/{id}` | USER/ADMIN | Delete owned content |
| `GET` | `/api/content/filter/keyword/{keyword}` | USER/ADMIN | Search user content by title keyword |
| `GET` | `/api/content/filter/status/{status}` | USER/ADMIN | Filter user content by status |

### Admin Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/admin/users` | ADMIN | Get all users (paginated/sorted) |
| `GET` | `/api/admin/content` | ADMIN | Get all content across users (paginated/sorted) |
| `DELETE` | `/api/admin/content/{id}` | ADMIN | Delete any content item by id |

## Pagination and Sorting

Query params:

- `page` (default `0`)
- `size` (default `10`)
- `sortBy` (example: `dateCreated`, `username`)
- `direction` (`asc` or `desc`)

Example:

```http
GET /api/content?page=0&size=10&sortBy=dateCreated&direction=desc
```

## Caching and Performance

- Redis cache enabled (`@EnableCaching`)
- User-scoped cache keys for single and paged content
- Eviction on create/update/delete

## Rate Limiting

Per-IP limits using `RateLimitFilter`:

- `/api/auth/**` -> 5 requests per minute
- `/api/content/**` -> 100 requests per minute

Exceeded limit response: `429 Too Many Requests`

## Environment Variables

Defined via `src/main/resources/application.properties` placeholders.

| Variable | Default | Purpose |
|---|---|---|
| `SPRING_APPLICATION_NAME` | `content-calender` | Application name |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/content_calender` | PostgreSQL URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | DB password |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | `org.postgresql.Driver` | JDBC driver |
| `SPRING_JPA_DATABASE_PLATFORM` | `org.hibernate.dialect.PostgreSQLDialect` | JPA dialect |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Schema mode |
| `SPRING_JPA_SHOW_SQL` | `false` | SQL logging |
| `SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL` | `true` | SQL formatting |
| `JWT_SECRET_KEY` | Required | JWT signing key |
| `APP_JWT_EXPIRATION_MS` | `900000` | Access token expiry |
| `APP_JWT_REFRESH_EXPIRATION_MS` | `604800000` | Refresh token expiry |
| `SPRING_CACHE_TYPE` | `redis` | Cache backend |
| `SPRING_DATA_REDIS_HOST` | `localhost` | Redis host |
| `SPRING_DATA_REDIS_PORT` | `6379` | Redis port |
| `APP_ADMIN_USERNAME` | `admin` | Admin seed username |
| `APP_ADMIN_PASSWORD` | `admin123` | Admin seed password |

## Run with Docker

`docker-compose.yml` runs:

- `app` (Spring Boot API)
- `postgres` (PostgreSQL 16)
- `redis` (Redis 7)

```powershell
.\mvnw.cmd clean package -DskipTests
docker compose up --build
```

Base URL: `http://localhost:8080`

## Run Locally

Start PostgreSQL and Redis locally, then run:

```powershell
.\mvnw.cmd clean package -DskipTests
.\mvnw.cmd spring-boot:run
```

## Error Handling

`GlobalExceptionHandler` returns structured `ApiError`:

- `error`
- `statusCode`
- `timestamp`

## Deployment Notes

- Keep `.env` out of version control
- Use strong production secrets for `JWT_SECRET_KEY` and admin credentials
- Set `secure=true` for auth cookie when running behind HTTPS
