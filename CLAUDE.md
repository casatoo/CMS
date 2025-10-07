# CMS - Content Management System

## Overview
This is a Spring Boot-based Content Management System (CMS) focused on employee attendance management and user administration. The system provides web-based interfaces for tracking attendance, managing leave requests, and user authentication.

## Technology Stack

### Backend Framework
- **Spring Boot 3.2.5** - Main application framework
- **Java 17** - Programming language (Amazon Corretto 17.0.10)
- **Gradle 8.7** - Build tool and dependency management

### Spring Framework Components
- **Spring Security 6** - Authentication and authorization with JWT
- **Spring Data JPA** - Database abstraction layer with Hibernate
- **Spring Web MVC** - Web layer and REST API
- **Spring WebFlux** - Reactive programming support
- **MyBatis 3.0.3** - SQL mapping framework (used alongside JPA)
- **Thymeleaf** - Server-side template engine
- **Spring Session** - Session management

### Database & Persistence
- **MySQL/MariaDB** - Primary database (MariaDB dialect)
- **JPA/Hibernate** - ORM with DDL auto-update enabled
- **MyBatis** - Custom SQL queries and complex operations

### Security & Authentication
- **JWT (JSON Web Tokens)** - Token-based authentication
- **BCrypt** - Password encryption
- **Spring Security** - Comprehensive security framework

### Resilience & Monitoring
- **Resilience4j** - Circuit breaker, retry, and bulkhead patterns
- **Spring Boot Actuator** - Application monitoring and health checks

### Additional Libraries
- **Lombok** - Code generation for reducing boilerplate
- **ModelMapper** - Object mapping utility
- **Thymeleaf Layout Dialect** - Template layout management

## Open Source Libraries Used

### Core Spring Framework
- **Spring Boot 3.2.5** - Main application framework with auto-configuration
- **Spring Data JPA** - Data access layer with repository pattern
- **Spring Data JDBC** - JDBC-based data access
- **Spring Security 6** - Authentication and authorization framework
- **Spring Web MVC** - Web framework for REST APIs and controllers
- **Spring WebFlux** - Reactive web framework
- **Spring Web Services** - SOAP web services support
- **Spring Session Core** - Session management
- **Spring Cloud Dependencies 2023.0.1** - Cloud-native patterns

### Database & Persistence
- **MySQL Connector/J** - MySQL database driver
- **MyBatis Spring Boot Starter 3.0.3** - SQL mapping framework integration
- **Hibernate/JPA** - Object-relational mapping (included in Spring Data JPA)

### Security & Authentication
- **JJWT 0.12.3** - JWT library for token creation and validation
  - `jjwt-api` - JWT API
  - `jjwt-impl` - JWT implementation
  - `jjwt-jackson` - Jackson integration for JSON processing
- **Thymeleaf Extras Spring Security 6** - Thymeleaf integration with Spring Security

### Resilience & Circuit Breaker
- **Resilience4j** - Fault tolerance library
  - `spring-cloud-starter-circuitbreaker-resilience4j` - Circuit breaker for blocking calls
  - `spring-cloud-starter-circuitbreaker-reactor-resilience4j` - Circuit breaker for reactive calls

### Template Engine & Frontend
- **Thymeleaf** - Server-side template engine (included in Spring Boot starter)
- **Thymeleaf Layout Dialect** - Layout management for Thymeleaf templates
- **AG-Grid Community** - Advanced data grid component for displaying tabular data
  - Located at: `/src/main/resources/static/js/aggrid/ag-grid-community.min.js`
  - CSS theme: `/src/main/resources/static/css/aggrid/ag-theme-alpine.css`

### Utilities & Tools
- **Lombok** - Annotation-based code generation to reduce boilerplate
- **ModelMapper 3.2.0** - Object-to-object mapping utility
- **Spring Boot DevTools** - Development-time tools (hot reload, etc.)
- **Spring Boot Configuration Processor** - Configuration metadata generation

### Logging
- **Logback Classic** - Logging implementation
- **SLF4J API** - Simple Logging Facade for Java

### Testing Framework
- **Spring Boot Starter Test** - Comprehensive testing starter including:
  - JUnit 5 (Jupiter)
  - Mockito
  - AssertJ
  - Hamcrest
  - Spring Test & Spring Boot Test
- **Reactor Test** - Testing utilities for reactive streams
- **Spring Security Test** - Security testing utilities
- **JUnit Platform Launcher** - Test runtime

### Build & Development Tools
- **Gradle 8.7** - Build automation tool
- **Spring Dependency Management Plugin 1.1.7** - Dependency version management

## Project Structure

```
src/
├── main/
│   ├── java/io/github/mskim/comm/cms/
│   │   ├── CmsApplication.java           # Main Spring Boot application
│   │   ├── api/                          # External API integrations
│   │   ├── config/                       # Configuration classes
│   │   │   ├── SecurityConfig.java       # Spring Security configuration
│   │   │   ├── CorsProperties.java       # CORS configuration
│   │   │   ├── ModelMapperConfig.java    # ModelMapper configuration
│   │   │   └── ThymeleafConfig.java      # Thymeleaf configuration
│   │   ├── controller/                   # Web controllers
│   │   │   ├── api/                      # REST API controllers
│   │   │   └── view/                     # View controllers (Thymeleaf)
│   │   ├── dto/                          # Data Transfer Objects
│   │   ├── entity/                       # JPA entities
│   │   ├── jwt/                          # JWT utilities and filters
│   │   ├── mapper/                       # MyBatis mappers
│   │   ├── repository/                   # JPA repositories
│   │   ├── service/                      # Service interfaces
│   │   ├── serviceImpl/                  # Service implementations
│   │   └── sp/                           # Stored procedure related
│   └── resources/
│       ├── application.yml               # Main configuration
│       ├── mapper/                       # MyBatis XML mappers
│       ├── static/                       # Static web resources
│       │   ├── css/                      # Stylesheets (Bootstrap 5.3.3)
│       │   ├── js/                       # JavaScript files
│       │   ├── img/                      # Images
│       │   ├── fonts/                    # Web fonts
│       │   └── svg/                      # SVG icons
│       └── templates/                    # Thymeleaf templates
│           ├── attendance/               # Attendance management views
│           ├── fragments/                # Reusable template fragments
│           ├── layout/                   # Layout templates
│           └── main/                     # Main application views
└── test/                                 # Test sources
```

## Core Entities & Domain Model

### User Management
- **Users** - Core user entity with login credentials and profile
- **UserLoginHistory** - Track user login activities

### Attendance Management
- **UserAttendance** - Daily attendance records (check-in/check-out)
- **UserAttendanceChangeRequest** - Requests for attendance modifications
- **UserLeaveRequest** - Leave/vacation requests

## Key Features

### Authentication & Security
- JWT-based stateless authentication
- Cookie-based token storage (HttpOnly, Secure)
- Role-based access control
- CORS configuration for multiple domains
- Custom authentication entry points

### Attendance System
- Daily check-in/check-out tracking
- 8-hour work day calculation
- Monthly work day counting
- Attendance modification requests
- Leave request management

### Resilience Patterns
- Circuit breaker for external service calls
- Retry mechanisms with configurable attempts
- Bulkhead pattern for request isolation
- Thread pool management for concurrent requests

## Available Gradle Tasks

### Development Tasks
```bash
./gradlew bootRun              # Run the Spring Boot application
./gradlew bootTestRun          # Run with test runtime classpath
./gradlew build                # Build the project
./gradlew test                 # Run tests
./gradlew bootJar              # Create executable JAR
./gradlew bootBuildImage       # Build OCI/Docker image
```

### Development Workflow
```bash
# Start development server
./gradlew bootRun

# Build for production
./gradlew build

# Run tests
./gradlew test
```

## Configuration

### Database Configuration
- **Database**: MySQL/MariaDB on localhost:3306
- **Schema**: cms
- **Connection**: UTF-8 encoding with Asia/Seoul timezone
- **JPA**: DDL auto-update enabled for development

### Server Configuration
- **Port**: 8080
- **Session Timeout**: 30 minutes
- **Error Handling**: Custom error pages enabled

### Security Configuration
- **JWT Secret**: Configured in application.yml
- **CORS Origins**: localhost, 127.0.0.1, and specific IP addresses
- **Session**: Stateless (JWT-based)

## Development Environment Setup

### Prerequisites
- JDK 17 (Amazon Corretto recommended)
- MySQL/MariaDB server
- Gradle 8.7 (or use wrapper)
- IntelliJ IDEA 2021.2.1+ (recommended)

### IDE Configuration
- Enable UTF-8 file encoding
- Set Build Tool to Gradle IntelliJ IDEA
- Disable Async Stack Traces
- Disable Launch Optimization

### Database Setup
1. Install MySQL/MariaDB
2. Create database named 'cms'
3. Update credentials in application.yml if needed
4. JPA will auto-create tables on first run

## Architecture Patterns

### Layered Architecture
- **Controller Layer**: Handle HTTP requests (View + API controllers)
- **Service Layer**: Business logic implementation
- **Repository Layer**: Data access (JPA + MyBatis)
- **Entity Layer**: Domain models and database entities

### Security Architecture
- JWT filter chain for stateless authentication
- Custom authentication entry points
- Role-based access control
- CORS handling for cross-origin requests

### Resilience Architecture
- Circuit breaker pattern for external dependencies
- Retry mechanisms for transient failures
- Bulkhead pattern for resource isolation
- Health check endpoints via Actuator

## Key Files to Understand

### Configuration Files
- `/src/main/resources/application.yml` - Main application configuration
- `/src/main/java/io/github/mskim/comm/cms/config/SecurityConfig.java` - Security setup
- `/build.gradle` - Project dependencies and build configuration

### Core Application Files
- `/src/main/java/io/github/mskim/comm/cms/CmsApplication.java` - Application entry point
- `/src/main/java/io/github/mskim/comm/cms/controller/view/MainViewController.java` - Main page controller
- `/src/main/java/io/github/mskim/comm/cms/jwt/` - JWT authentication components

### Frontend Resources
- `/src/main/resources/templates/` - Thymeleaf templates
- `/src/main/resources/static/` - CSS, JS, images, and other static assets

## Development Notes

### Code Style & Conventions
- Uses Lombok for reducing boilerplate code
- Constructor injection preferred over field injection
- Service layer uses interface + implementation pattern
- MyBatis XML mappers for complex SQL queries
- Thymeleaf for server-side rendering

### Testing
- JUnit 5 platform for testing
- Spring Boot Test for integration tests
- Reactor Test for reactive components

### Monitoring & Observability
- Spring Boot Actuator endpoints enabled
- Circuit breaker health indicators
- Detailed error handling with custom error controller

This codebase follows Spring Boot best practices with a focus on enterprise-grade features like security, resilience, and monitoring. The hybrid approach of using both JPA and MyBatis provides flexibility for both simple CRUD operations and complex queries.

## Recent Security Improvements (2025-10-03)

### Critical Security Issues Fixed
The following security vulnerabilities have been identified and resolved:

1. **JWT Token Expiration Bug Fixed** (`JWTUtil.java:37`)
   - **Issue**: Token expiration calculation multiplied by 100, causing tokens to expire much later than intended
   - **Fix**: Removed the `* 100` multiplier to use correct millisecond values
   - **Impact**: Tokens now expire at the intended time, preventing unauthorized extended access

2. **Environment Variable Externalization** (`application.yml`)
   - **Issue**: Hardcoded database credentials and JWT secrets in configuration files
   - **Fix**: Externalized sensitive values to environment variables
   - **Variables**: `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `DDL_AUTO`
   - **Added**: `.env.example` file for environment setup guidance

3. **Cookie Security Implementation Fixed** (`JWTFilter.java:89`)
   - **Issue**: Wrong token used in cookie (old token instead of new token)
   - **Fix**: Updated to use new token in cookie storage
   - **Enhancement**: Made secure cookie setting conditional based on request protocol

4. **Comprehensive Input Validation Added**
   - **Controllers**: Added `@Validated` and `@Valid` annotations to all API controllers
   - **DTOs**: Added validation constraints to all data transfer objects
   - **Global Exception Handler**: Created centralized validation error handling
   - **Features**:
     - Strong password policy (8+ chars, mixed case, numbers, special chars)
     - Login ID format validation (alphanumeric only)
     - Date format validation (yyyy-MM-dd pattern)
     - Field length and pattern validations
     - Korean error messages for better UX

### Security Best Practices Implemented

#### Password Security
- Minimum 8 characters required
- Must contain uppercase, lowercase, numbers, and special characters
- Login IDs restricted to alphanumeric characters only

#### Input Sanitization
- All API endpoints now validate input parameters
- Request body validation with detailed error messages
- Parameter format validation (dates, IDs, etc.)

#### Environment Configuration
- All sensitive configuration externalized
- Production-ready environment variable setup
- Separate configuration for different environments

### Environment Setup
To run this application securely, set the following environment variables:

```bash
# Database Configuration
export DB_USERNAME=your_database_username
export DB_PASSWORD=your_secure_database_password

# JWT Configuration
export JWT_SECRET=your_strong_jwt_secret_key

# Application Configuration
export DDL_AUTO=update  # Use 'validate' for production
```

### Security Validation Commands
After setting up environment variables, verify the application starts correctly:
```bash
# Start the application
./gradlew bootRun

# Verify security endpoints
curl -X POST http://localhost:8080/joinProc \
  -H "Content-Type: application/json" \
  -d '{"loginId":"test","password":"short"}'
# Should return validation error

# Test attendance API validation
curl "http://localhost:8080/api/attendance/month/all?startDate=invalid&endDate=2024-12-31"
# Should return date format validation error
```

### Files Modified for Security
- `/src/main/java/io/github/mskim/comm/cms/jwt/JWTUtil.java` - Fixed token expiration
- `/src/main/java/io/github/mskim/comm/cms/jwt/JWTFilter.java` - Fixed cookie implementation
- `/src/main/resources/application.yml` - Externalized configuration
- `/src/main/java/io/github/mskim/comm/cms/controller/api/AttendanceApiController.java` - Added validation
- `/src/main/java/io/github/mskim/comm/cms/controller/api/JoinApiController.java` - Added validation
- `/src/main/java/io/github/mskim/comm/cms/dto/JoinDTO.java` - Added validation constraints
- `/src/main/java/io/github/mskim/comm/cms/dto/UserAttendanceChangeRequestDTO.java` - Added validation constraints
- `/src/main/java/io/github/mskim/comm/cms/exception/GlobalExceptionHandler.java` - New file for error handling
- `/.env.example` - New file for environment variable guidance

### Important Security Notes
- **Production Deployment**: Always use `DDL_AUTO=validate` in production environments
- **HTTPS Required**: Set secure cookies only work properly with HTTPS in production
- **Secret Management**: Use proper secret management systems (e.g., HashiCorp Vault) in production
- **Regular Updates**: Keep dependencies updated and monitor for security vulnerabilities

This codebase now follows security best practices and is production-ready with proper input validation, secure configuration management, and robust error handling.