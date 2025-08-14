# MediSync

A Java‑based healthcare management system that streamlines patient records, appointments, and clinician workflows with secure, role‑based access.

[![Java](https://img.shields.io/badge/Java-17+-red.svg)](https://adoptium.net/)  
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)  
[![Build](https://github.com/Shailendra17103/MediSync/actions/workflows/build.yml/badge.svg)](https://github.com/Shailendra17103/MediSync/actions)  
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](#license)

> **Highlights:** JWT auth, RBAC, audit logging, DTO validation, OpenAPI docs, Dockerized Postgres, CI ready.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Screenshots / Diagrams](#screenshots--diagrams)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Clone](#clone)
  - [Configuration](#configuration)
  - [Environment Variables](#environment-variables)
  - [Database Setup](#database-setup)
  - [Run (Local Dev)](#run-local-dev)
  - [Run with Docker Compose](#run-with-docker-compose)
  - [Verify](#verify)
- [Usage](#usage)
  - [API Quick Reference](#api-quick-reference)
  - [Sample Requests](#sample-requests)
  - [API Docs / Swagger](#api-docs--swagger)
- [Testing](#testing)
- [Code Quality](#code-quality)
- [Migrations](#migrations)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [Security](#security)
- [Configuration Reference](#configuration-reference)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Overview

MediSync centralizes patient data, appointments, clinicians, and administrative operations. It provides secure authentication, audit logging, and a clean API for integrations.

## Features

- Patient profile and EMR management
- Appointment scheduling with calendar‑friendly payloads
- Role‑based access control (Admin, Doctor, Receptionist)
- Authentication/authorization (JWT; OAuth2‑ready)
- Search/filtering for patients & appointments
- Audit logs for sensitive actions
- Import/export (CSV/JSON) for records
- Strong validations, error contracts, and problem details

## Architecture

- Layered: **Controller → Service → Repository**
- REST with DTOs + Bean Validation
- JPA/Hibernate; database‑agnostic (PostgreSQL/MySQL/H2)
- Optional: caching (Caffeine/Redis), messaging, object storage
- OpenAPI via springdoc for live API docs
- Profiles: `dev`, `test`, `prod`

## Tech Stack

- **Core:** Java 17+, Spring Boot 3.x, Spring Data JPA, Spring Security  
- **Build:** Maven (default) or Gradle  
- **DB:** PostgreSQL (prod), H2 (dev/test)  
- **Test:** JUnit 5, Spring Test, Mockito  
- **Lint/Format:** Checkstyle, Spotless  
- **Docs:** springdoc‑openapi (Swagger UI)

## Screenshots / Diagrams

- ERD: `docs/erd.png`  
- High‑level architecture: `docs/architecture.png`  
- UI: `docs/screens/*.png`

> Ensure these files exist at the given paths to render on GitHub.

## Getting Started

### Prerequisites

- Java 17+
- Git
- PostgreSQL (or Docker)
- Maven Wrapper `./mvnw` (or Gradle Wrapper `./gradlew`)

### Clone

SSH:
```bash
git clone git@github.com:Shailendra17103/MediSync.git
cd MediSync
```

HTTPS:
```bash
git clone https://github.com/Shailendra17103/MediSync.git
cd MediSync
```

### Configuration

Create `src/main/resources/application.yml` (or use `application.properties`):

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/medisync}
    username: ${DB_USERNAME:medisync}
    password: ${DB_PASSWORD:medisync}
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
    properties:
      hibernate:
        format_sql: true
  jackson:
    serialization:
      write-dates-as-timestamps: false
server:
  port: ${SERVER_PORT:8080}

app:
  security:
    jwtSecret: ${JWT_SECRET:change-me}
    jwtExpirationMinutes: ${JWT_EXPIRATION_MINUTES:60}
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

### Environment Variables

Bash:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/medisync
export DB_USERNAME=medisync
export DB_PASSWORD=medisync
export JWT_SECRET=super-secret-change-me
export JWT_EXPIRATION_MINUTES=60
```

PowerShell:
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/medisync"
$env:DB_USERNAME="medisync"
$env:DB_PASSWORD="medisync"
$env:JWT_SECRET="super-secret-change-me"
$env:JWT_EXPIRATION_MINUTES="60"
```

Optional `.env` file (Docker/Compose/deployment):
```
DB_URL=jdbc:postgresql://db:5432/medisync
DB_USERNAME=medisync
DB_PASSWORD=medisync
JWT_SECRET=please-change-in-prod
JWT_EXPIRATION_MINUTES=60
SERVER_PORT=8080
```

### Database Setup

Dockerized Postgres:
```bash
docker run --name medisync-db   -e POSTGRES_USER=medisync   -e POSTGRES_PASSWORD=medisync   -e POSTGRES_DB=medisync   -p 5432:5432 -d postgres:16
```

Native:
1. Create database `medisync`
2. Create user/password `medisync`/`medisync` (or update env)

### Run (Local Dev)

Maven:
```bash
./mvnw clean spring-boot:run
# Windows: mvnw.cmd clean spring-boot:run
```

Gradle:
```bash
./gradlew bootRun
# Windows: gradlew.bat bootRun
```

### Run with Docker Compose

`docker-compose.yml` (place in repo root):

```yaml
version: "3.9"
services:
  db:
    image: postgres:16
    container_name: medisync-db
    environment:
      POSTGRES_USER: ${DB_USERNAME:-medisync}
      POSTGRES_PASSWORD: ${DB_PASSWORD:-medisync}
      POSTGRES_DB: ${POSTGRES_DB:-medisync}
    ports:
      - "5432:5432"
    volumes:
      - dbdata:/var/lib/postgresql/data
  app:
    build: .
    container_name: medisync-app
    env_file: .env
    ports:
      - "${SERVER_PORT:-8080}:8080"
    depends_on:
      - db
volumes:
  dbdata:
```

Build & up:
```bash
docker compose up --build
```

### Verify

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html  
- **Health:** http://localhost:8080/actuator/health

## Usage

### API Quick Reference

- **Auth**
  - `POST /api/auth/register`
  - `POST /api/auth/login`
- **Patients**
  - `GET /api/patients`
  - `POST /api/patients`
  - `GET /api/patients/{id}`
  - `PUT /api/patients/{id}`
  - `DELETE /api/patients/{id}`
- **Appointments**
  - `GET /api/appointments`
  - `POST /api/appointments`
  - `PUT /api/appointments/{id}`
  - `DELETE /api/appointments/{id}`

### Sample Requests

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login   -H "Content-Type: application/json"   -d '{"username":"doctor1","password":"password"}'

# List patients (with JWT)
curl http://localhost:8080/api/patients   -H "Authorization: Bearer <JWT_TOKEN>"
```

### API Docs / Swagger

- Auto‑generated via springdoc‑openapi at `/v3/api-docs` and `/swagger-ui/index.html`.
- Add Postman collection by exporting `/v3/api-docs` to JSON (many plugins support this).

## Testing

```bash
./mvnw test
# or
./gradlew test
```

- Coverage (if Jacoco enabled): `target/site/jacoco/index.html` or `build/reports/jacoco/index.html`

## Code Quality

Format / lint:
```bash
./mvnw spotless:apply
# or
./gradlew spotlessApply
```

Static analysis:
- Checkstyle rules in `config/`
- (Optional) add PMD/SpotBugs plugins

## Migrations

Use **Flyway** or **Liquibase** to version your schema:

- Flyway location: `src/main/resources/db/migration/V1__init.sql`
- Enable in `application.yml`:
  ```yaml
  spring:
    flyway:
      enabled: true
      locations: classpath:db/migration
  ```
- Replace `ddl-auto: update` with `ddl-auto: validate` in production.

## Project Structure

```
src/
  main/
    java/com/example/medisync/...
    resources/
      application.yml
      db/migration/           # (Flyway)
  test/
docs/
  erd.png
  architecture.png
```

## Database Schema

Core tables:
- `patients`, `clinicians`, `appointments`, `users`, `roles`, `audit_logs`

See `docs/erd.png` for relationships.

## Security

- JWT auth; BCrypt password hashing
- Role‑based authorization (method and endpoint level)
- Input validation, exception handling with consistent error responses
- **Production:** externalize secrets, enforce HTTPS/secure cookies, rotate keys

## Configuration Reference

- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET`, `JWT_EXPIRATION_MINUTES`
- `SERVER_PORT` (optional)
- Spring profile: `spring.profiles.active=dev|prod`

## Deployment

### Docker (single image)
```bash
docker build -t medisync:latest .
docker run --rm -p 8080:8080 --env-file .env medisync:latest
```

### Profiles
- `dev`: H2 or local Postgres, verbose logs
- `prod`: Postgres, Flyway validate/migrate, reduced logs

## Troubleshooting

| Issue | Cause | Fix |
|---|---|---|
| Git `Permission denied (publickey)` | SSH key not added/agent not running | `ssh-add` key; `ssh -T git@github.com` or use HTTPS remote |
| DB connection refused | DB not running / wrong URL | Start Postgres/Compose; verify `DB_URL` |
| `401 Unauthorized` | Missing/expired token | Re‑login; check `Authorization: Bearer <JWT>` |
| Swagger not loading | springdoc not on classpath | Add `org.springdoc:springdoc-openapi-starter-webmvc-ui` |
| App fails on prod | `ddl-auto=update` misuse | Use Flyway; set `ddl-auto=validate` |

## Roadmap

- Advanced reporting & analytics
- Email/SMS notifications
- EHR interoperability (HL7/FHIR)
- Multi‑tenancy and enhanced auditing

## Contributing

1. Fork and create a feature branch
2. Commit with clear messages
3. Open a PR with description/screenshots
4. Follow code style; add/maintain tests

## License

MIT — see [LICENSE](LICENSE).

## Contact

- Maintainer: **@Shailendra17103**  
- Email: **shailendrabhushan17@gmail.com**
