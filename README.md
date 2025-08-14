# MediSync

A Java-based healthcare management system that streamlines patient records, appointments, and clinician workflows with secure, role-based access.

[![Java](https://img.shields.io/badge/Java-17+-red.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build](https://github.com/Shailendra17103/MediSync/actions/workflows/build.yml/badge.svg)](https://github.com/Shailendra17103/MediSync/actions)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](#license)

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
  - [Database Setup](#database-setup)
  - [Build and Run](#build-and-run)
  - [Verify](#verify)
- [Usage](#usage)
  - [API Quick Reference](#api-quick-reference)
  - [Sample Requests](#sample-requests)
- [Testing](#testing)
- [Code Quality](#code-quality)
- [CI/CD](#cicd)
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
- Appointment scheduling and calendar view
- Role-based access control (Admin, Doctor, Receptionist)
- Authentication/authorization (JWT/OAuth2-ready)
- Search and filtering for patients/appointments
- Audit logs for critical actions
- Import/export (CSV/JSON) for records
- Robust validations and error handling

## Architecture
- Layered architecture (Controller → Service → Repository)
- RESTful APIs with DTOs and validation
- JPA/Hibernate for ORM; database-agnostic (PostgreSQL/MySQL/H2)
- Optional: Caching (e.g., Caffeine/Redis), Messaging, and File storage

## Tech Stack
- Java 17+, Spring Boot 3.x, Spring Data JPA, Spring Security
- Build: Maven or Gradle
- DB: PostgreSQL (prod), H2 (dev/test)
- Test: JUnit 5, Spring Test, Mockito
- Lint/Format: Checkstyle/Spotless
- Docs: OpenAPI/Swagger

## Screenshots / Diagrams
- ERD: docs/erd.png
- High-level architecture: docs/architecture.png
- UI screenshots: docs/screens/*.png

## Getting Started

### Prerequisites
- Java 17+
- Git
- PostgreSQL (or use Docker)
- Maven Wrapper (./mvnw) or Gradle Wrapper (./gradlew)

### Clone
```bash
git clone git@github.com:Shailendra17103/MediSync.git
cd MediSync
```

### Configuration
Create src/main/resources/application.yml (or use application.properties):
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/medisync}
    username: ${DB_USERNAME:medisync}
    password: ${DB_PASSWORD:medisync}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
  jackson:
    serialization:
      write-dates-as-timestamps: false

app:
  security:
    jwtSecret: ${JWT_SECRET:change-me}
    jwtExpirationMinutes: 60
server:
  port: 8080
```

Environment variables (local dev):
```bash
export DB_URL=jdbc:postgresql://localhost:5432/medisync
export DB_USERNAME=medisync
export DB_PASSWORD=medisync
export JWT_SECRET=super-secret-change-me
```
Windows (PowerShell):
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/medisync"
$env:DB_USERNAME="medisync"
$env:DB_PASSWORD="medisync"
$env:JWT_SECRET="super-secret-change-me"
```

### Database Setup
- Create DB/user OR run Docker:
```bash
docker run --name medisync-db -e POSTGRES_USER=medisync -e POSTGRES_PASSWORD=medisync -e POSTGRES_DB=medisync -p 5432:5432 -d postgres:16
```

### Build and Run
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

### Verify
- OpenAPI/Swagger UI: http://localhost:8080/swagger-ui/index.html
- Health endpoint: http://localhost:8080/actuator/health

## Usage

### API Quick Reference
- Auth:
  - POST /api/auth/register
  - POST /api/auth/login
- Patients:
  - GET /api/patients
  - POST /api/patients
  - GET /api/patients/{id}
  - PUT /api/patients/{id}
  - DELETE /api/patients/{id}
- Appointments:
  - GET /api/appointments
  - POST /api/appointments
  - PUT /api/appointments/{id}
  - DELETE /api/appointments/{id}

### Sample Requests
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor1","password":"password"}'

# List patients (with JWT)
curl http://localhost:8080/api/patients \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

## Testing
```bash
# Unit + integration tests
./mvnw test
# or
./gradlew test
```
- Coverage via Jacoco (optional): open build/reports/jacoco/index.html or target/site/jacoco/index.html

## Code Quality
- Format/lint:
```bash
./mvnw spotless:apply
# or
./gradlew spotlessApply
```
- Static analysis: Checkstyle rules in config/

## CI/CD
- GitHub Actions workflow (.github/workflows/build.yml) runs tests and checks on push/PR.
- Optional: Docker image build and publish.

## Project Structure
```
src/
  main/
    java/com/example/medisync/...
    resources/
      application.yml
  test/
docs/
  erd.png
  architecture.png
```

## Database Schema
- Core tables: patients, clinicians, appointments, users, roles, audit_logs
- See docs/erd.png for relationships.

## Security
- JWT-based authentication, password hashing (BCrypt)
- Role-based authorization (method and endpoint level)
- Input validation, error handling, and audit logging
- Note: Ensure environment-specific secrets and HTTPS in production

## Configuration Reference
- DB_URL, DB_USERNAME, DB_PASSWORD
- JWT_SECRET, JWT_EXPIRATION_MINUTES
- SERVER_PORT (optional)

## Deployment
- Docker:
```bash
docker build -t medisync:latest .
docker run -p 8080:8080 --env-file .env medisync:latest
```
- Profiles: use spring.profiles.active=prod for production configs

## Troubleshooting
- Permission denied (publickey) with Git:
  - Add your SSH key to the agent and GitHub; run `ssh -T git@github.com`
- DB connection refused:
  - Ensure DB is running and DB_URL is correct
- 401 Unauthorized:
  - Check Authorization header and token expiry

## Roadmap
- Advanced reports and analytics
- Notifications (email/SMS)
- EHR interoperability (HL7/FHIR)
- Multi-tenancy and auditing improvements

## Contributing
1. Fork, create feature branch
2. Commit with clear messages
3. Open PR with description and screenshots
4. Follow code style and add tests

## License
This project is licensed under the MIT License — see [LICENSE](LICENSE).

## Contact
- Maintainer: @Shailendra17103
- Email: shailendrabhushan17@gmail.com
