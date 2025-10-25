🧠 Identity Access Governance (IAM) System

A Spring Boot + MySQL based project demonstrating key principles of Identity and Access Management (IAM) and Governance — including user onboarding, role-based access control, access request workflows, Segregation of Duties (SoD) checks, and detailed audit logging.

Built to showcase enterprise-grade access management logic, this project helps understand how organizations manage, govern, and secure user access across systems.

🚀 Features

✅ User Identity Management — Register, view, and manage user accounts.
✅ Role-Based Access Control (RBAC) — Create roles and assign them securely.
✅ Access Request Workflow — Users can raise, approve, or reject role access requests.
✅ Segregation of Duties (SoD) — Prevents conflicting roles (e.g., Finance + Audit).
✅ Audit & Compliance Logging — Every action (approve/reject/login) is logged.
✅ Spring Security Authentication — Basic Auth with encrypted passwords (BCrypt).
✅ Exception Handling — Clean API responses and validation messages.

🧩 Tech Stack
Layer	Technology
Backend Framework	Spring Boot (v3.5.x)
Language	Java 17
Database	MySQL 8.0
ORM	Hibernate + Spring Data JPA
Security	Spring Security (HTTP Basic Auth)
Build Tool	Maven
⚙️ Setup Instructions
1️⃣ Clone the Repository
git clone https://github.com/YOUR_GITHUB_USERNAME/identity-access-governance.git
cd identity-access-governance

2️⃣ Create MySQL Database

Login to MySQL and run:

CREATE DATABASE iam_system;

3️⃣ Configure Application Properties

Open src/main/resources/application.properties and update your credentials:

spring.datasource.url=jdbc:mysql://localhost:3306/iam_system
spring.datasource.username=root
spring.datasource.password=root1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

server.port=8080

4️⃣ Run the Application
./mvnw spring-boot:run


Then open your browser at 👉 http://localhost:8080

🧮 Sample API Endpoints
Method	Endpoint	Description
POST	/api/users/register	Register a new user
GET	/api/users/{username}	Fetch user details by username
POST	/api/roles	Create a new role
GET	/api/roles	View all roles
POST	/api/access/request	Raise access request for a role
PUT	/api/access/approve/{id}	Approve access request
PUT	/api/access/reject/{id}	Reject access request
GET	/api/audit/logs	Retrieve audit/compliance logs
🔐 Default Security
Username	Password	Role
admin	admin123	ADMIN

You can change this in:

config/SecurityConfig.java

🧠 Concept Overview
🔹 Identity Management

Creation, authentication, and lifecycle management of user identities.

🔹 Access Governance

Ensures only authorized users receive specific access rights.

🔹 Segregation of Duties (SoD)

Prevents conflict by blocking role combinations that violate governance rules (e.g., “ROLE_FINANCE” + “ROLE_AUDIT”).

🔹 Audit & Compliance

Every decision (request/approve/reject) is logged for tracking and security audits.

🗂 Folder Structure
src/
 ├── main/
 │   ├── java/com/iamdemo/identity_access_governance/
 │   │   ├── model/           # Entity classes
 │   │   ├── repository/      # JPA Repositories
 │   │   ├── service/         # Business logic (IAM, SoD, audit)
 │   │   ├── controller/      # REST endpoints
 │   │   └── config/          # Security configuration
 │   └── resources/
 │       └── application.properties
 └── test/

🧾 Example JSON Payloads
🔸 User Registration
{
  "username": "shruti",
  "email": "shruti@example.com",
  "password": "mypassword"
}

🔸 Access Request
{
  "username": "shruti",
  "role": "ROLE_FINANCE",
  "reason": "Need access for reporting module"
}

🔍 Testing the APIs

You can use Postman or cURL to test endpoints.

Example request:

curl -X POST http://localhost:8080/api/users/register \
-H "Content-Type: application/json" \
-d '{"username": "shruti", "email": "shruti@example.com", "password": "mypassword"}'

🌟 Future Enhancements

🚀 JWT Authentication with refresh tokens
📊 React.js Admin Dashboard for access governance visualization
☁️ AWS Deployment (RDS + EC2 + S3)
🧩 Role Review & Recertification Workflow

👩‍💻 Author

Shruti Mishra