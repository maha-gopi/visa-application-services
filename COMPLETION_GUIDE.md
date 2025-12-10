# Microservices Completion Guide

## Status

### ✅ Completed Services
1. **visa-types-service** - Fully implemented
2. **services-service** - Fully implemented

### ⏳ Remaining Services
3. **appointments-service** - Directory structure created
4. **bookings-service** - Directory structure created  
5. **payments-service** - Directory structure created
6. **utilities-service** - Directory structure created

## Pattern to Follow

All services follow the same structure. Use the completed services as templates.

### 1. Create pom.xml
Copy from `visa-types-service/pom.xml` and update:
- `<artifactId>` - service name
- `<name>` - service display name
- `<description>` - service description

### 2. Create application.yml
Copy from `visa-types-service/src/main/resources/application.yml` and update:
- `spring.application.name` - service name
- `server.port` - unique port (8083, 8084, 8085, 8086)
- `otel.service.name` - service name

### 3. Create Application Class
```java
package com.vfsglobal.<service>;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class <Service>Application {
    public static void main(String[] args) {
        SpringApplication.run(<Service>Application.class, args);
    }
}
```

### 4. Create Entities
Based on DDL, create JPA entities in `entity/` package:
- Use `@Entity`, `@Table`, `@Id`, `@Column` annotations
- Map relationships with `@ManyToOne`, `@OneToMany`, etc.
- Use Lombok `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`

### 5. Create DTOs
Based on OpenAPI schemas, create DTOs in `dto/` package:
- Request DTOs for POST/PUT endpoints
- Response DTOs for GET endpoints
- Use Lombok `@Data`, `@Builder` annotations
- Use `@JsonInclude(JsonInclude.Include.NON_NULL)` for optional fields

### 6. Create Repositories
Extend `JpaRepository<Entity, ID>`:
- Add custom query methods using `@Query` annotation
- Use method naming conventions for simple queries

### 7. Create Services
Business logic layer:
- Use `@Service` annotation
- Inject repositories via constructor
- Use `@Transactional` for write operations
- Use `@Transactional(readOnly = true)` for read operations
- Add logging with `@Slf4j`

### 8. Create Controllers
REST endpoints:
- Use `@RestController`, `@RequestMapping`
- Add OpenAPI annotations (`@Operation`, `@ApiResponse`, `@Tag`)
- Use `@PathVariable`, `@RequestParam`, `@RequestBody`
- Return `ResponseEntity<T>`

### 9. Copy Common Files
From `visa-types-service`:
- `exception/GlobalExceptionHandler.java` (update package)
- `filter/CorrelationIdFilter.java` (update package)
- `resources/logback-spring.xml` (update service name)
- `Dockerfile` (update port if needed)

### 10. Create Flyway Migration
Create `V1__create_<service>_tables.sql` in `db/migration/`:
- Use `CREATE TABLE IF NOT EXISTS` for idempotency
- Create indexes
- Add constraints

## Service-Specific Details

### appointments-service (Port: 8083)

**Entities Needed:**
- Appointment
- AppointmentSlot
- Vac (Visa Application Center)
- BlackoutDate

**Key Endpoints:**
- GET /appointments/available-dates
- GET /appointments/available-time-slots
- POST /appointments/check-availability
- GET /appointments/{appointmentId}
- PUT /appointments/{appointmentId}
- DELETE /appointments/{appointmentId}

**DTOs Needed:**
- TimeSlot
- AvailabilityCheckRequest
- AvailabilityCheckResponse
- AppointmentResponse
- AppointmentUpdateRequest

---

### bookings-service (Port: 8084)

**Entities Needed:**
- Application
- Applicant
- ApplicationService
- VisaType (reference)
- ServiceCatalog (reference)

**Key Endpoints:**
- POST /bookings
- GET /bookings
- GET /bookings/{bookingId}
- PUT /bookings/{bookingId}
- DELETE /bookings/{bookingId}
- GET /bookings/reference/{referenceNumber}
- GET /bookings/{bookingId}/appointment-letter

**DTOs Needed:**
- BookingCreateRequest
- BookingUpdateRequest
- BookingResponse
- BookingSummary
- PersonalDetails
- SelectedService

---

### payments-service (Port: 8085)

**Entities Needed:**
- Payment
- PaymentLineItem
- Refund
- Application (reference)

**Key Endpoints:**
- POST /payments/process
- GET /payments/{paymentId}
- GET /payments/booking/{bookingId}
- POST /payments/{paymentId}/refund

**DTOs Needed:**
- PaymentRequest
- PaymentResponse
- PaymentError
- RefundRequest
- RefundResponse
- CardDetails

---

### utilities-service (Port: 8086)

**Entities Needed:**
- Country
- Vac
- ApplicantPhoto

**Key Endpoints:**
- GET /nationalities
- POST /photos/validate
- POST /photos/upload
- GET /locations
- GET /locations/{locationId}

**DTOs Needed:**
- NationalityResponse
- PhotoValidationRequest
- PhotoValidationResponse
- PhotoUploadResponse
- LocationResponse

**Special Considerations:**
- Photo validation requires file handling
- Photo upload requires multipart file support
- Use `@RequestPart` for file uploads

## Testing

After creating each service:

1. **Build:** `mvn clean package`
2. **Run:** `java -jar target/<service>-1.0.0.jar`
3. **Test Health:** `curl http://localhost:<port>/actuator/health`
4. **Test Swagger:** `http://localhost:<port>/swagger-ui.html`
5. **Test Endpoints:** Use Swagger UI or Postman

## Database Setup

1. Run the main DDL script: `VAMS_DDL_PostgreSQL.sql`
2. Run seed data scripts in order:
   - `01_master_data.sql`
   - `02_users_data.sql`
   - `03_operational_data.sql`
   - `04_sample_transactions.sql`
3. Each service will run Flyway migrations on startup

## Docker Build

For each service:
```bash
docker build -t <service-name>:1.0.0 .
docker run -p <port>:<port> <service-name>:1.0.0
```

## Common Issues & Solutions

1. **Port conflicts:** Ensure each service uses a unique port
2. **Database connection:** Check DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
3. **Package names:** Ensure all package declarations match directory structure
4. **Entity relationships:** Use `@ManyToOne(fetch = FetchType.LAZY)` to avoid N+1 queries
5. **DTO mapping:** Create mapper methods in service layer

## Next Steps After Completion

1. Create docker-compose.yml for all services
2. Add API Gateway (Spring Cloud Gateway)
3. Add Service Discovery (Eureka/Consul)
4. Add Configuration Server (Spring Cloud Config)
5. Add Circuit Breaker (Resilience4j)
6. Add Distributed Tracing (Jaeger/Zipkin)
7. Add API Documentation aggregation
8. Create integration tests
9. Set up CI/CD pipeline


