Identity Access Governance (Spring Boot)​
Production‑ready backend for managing users, roles, and access requests with approval workflows, Segregation of Duties (SoD) enforcement, audit logging, and basic authentication.​

Highlights​
Access request lifecycle with approval or rejection and automatic role assignment on approval.​

SoD policy engine to block conflicting combinations (e.g., Finance ↔ HR/Audit).​

Comprehensive audit trail for user/role creation, requests, approvals, rejections, and assignments.​

HTTP Basic for privileged endpoints and BCrypt password hashing on registration.​

Tech stack​
Java 17+, Spring Boot 3 (Web, Data JPA, Security, Validation), Maven.​

Relational database via JPA/Hibernate (e.g., MySQL).​

Project structure​
src/main/java/... application code organized by controller, service, model, repository.​

src/main/resources for configuration such as application.properties.​

pom.xml for dependencies and build configuration.​

Getting started​
Prerequisites: Java 17+, Maven wrapper, and a running SQL database.​

Clone and open the project at the root folder where pom.xml exists.​

Configuration​
Create or edit src/main/resources/application.properties as below.​

text
spring.datasource.url=jdbc:mysql://localhost:3306/iagdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=your_user
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
Build and run​
bash
mvnw clean compile
mvnw spring-boot:run
Security​
Approval endpoints under /api/requests/** require HTTP Basic authentication.​

A local admin user is configured for testing (username and password can be adjusted in security configuration).​

SoD rules (default)​
Conflicts enforced by the policy service: Finance ↔ HR and Finance ↔ Audit.​

Requests violating these pairs are rejected during approval.​

Domain model​
User: id, username, email, password (BCrypt), active, roles.​

Role: id, name (e.g., ROLE_ADMIN, ROLE_FINANCE, ROLE_HR, ROLE_AUDIT), description.​

AccessRequest: id, user, role, reason, status (PENDING/APPROVED/REJECTED), decidedBy, decidedAt.​

AuditLog: id, timestamp, actor, action, entityType, entityId, details.​

APIs​
Roles​
Create a role: POST /api/roles.​
Body:

json
{ "name": "ROLE_USER", "description": "Basic user role" }
List roles: GET /api/roles.​

Users​
Register user: POST /api/users/register.​
Body:

json
{
  "username": "shruti_demo",
  "email": "shruti_demo@example.com",
  "password": "Passw0rd!"
}
Get by username: GET /api/users/{username}.​

Access requests​
Create request: POST /api/requests.​
Body:

json
{
  "userId": 5,
  "roleId": 4,
  "reason": "Needs access for testing"
}
List pending: GET /api/requests/pending.​

Resolve (approve or reject): POST /api/requests/{id}/resolve?action=APPROVED&approver=admin (requires Basic Auth).​

Audit logs​
List logs: GET /api/auditlogs.​

Test flows​
Positive approval​
Ensure the target role is not already assigned and is not in the SoD conflict map (e.g., ROLE_ADMIN).​

Create the request and approve with Basic Auth to expect APPROVED and a role assignment audit.​

SoD rejection​
If the user has ROLE_FINANCE, request ROLE_HR or ROLE_AUDIT and approve to expect REJECTED with a SoD conflict reason.​

Duplicate role rejection​
Request a role the user already has and approve to expect REJECTED with an “Already has role” reason.​

Validation and errors​
409 Conflict on duplicate username, email, or role name.​

400 Bad Request on validation failures such as blank fields or invalid email.​

404 Not Found when resources do not exist.​

Postman​
Import a collection that covers register, create role, create request, approve or reject, and list audit logs.​

Use an environment with BASE_URL, ADMIN_USER, and ADMIN_PASS for quick testing.​

Troubleshooting​
401 Unauthorized on approve: add HTTP Basic credentials for the admin user.​

SoD not triggering: confirm exact role names match the policy and the user holds the conflicting role.​

Everything REJECTED: ensure the requested role is not already assigned and is not mapped as conflicting.​

Roadmap​
JWT/OAuth2, multi‑approver workflows, request expiration, periodic access reviews, CSV exports.​

Role ownership and approval routing rules

👩‍💻 Author

Shruti Mishra
