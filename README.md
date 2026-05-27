# 🚗 Velocity - Car Rental Management System

A full-stack Car Rental Management System with secure authentication, built using **Spring Boot** for the backend and **HTML/CSS/JavaScript** for the frontend.

---

## 🛠️ Tech Stack

### Backend
- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Redis (Caching)
- Spring Data JPA / Hibernate
- MySQL
- Maven

### Frontend
- HTML5
- CSS3
- JavaScript (Vanilla JS)

---

## ✨ Features

- 🔐 User Registration with OTP Email Verification
- 🔑 JWT-based Authentication & Authorization
- ⚡ Redis Caching for improved performance
- 🚗 Car Rental CRUD Operations
- 🌐 CORS Configuration for Frontend-Backend Integration
- 📊 Dashboard for managing rentals

---

## 📁 Project Structure

```
Velocity/
├── backend/          # Spring Boot Application
│   ├── src/
│   │   └── main/
│   │       ├── java/         # Java source files
│   │       └── resources/    # application.properties (not included)
│   └── pom.xml
│
├── frontend/         # Vanilla JS Frontend
│   ├── login/
│   │   ├── login.html
│   │   ├── login.css
│   │   └── login.js
│   ├── register/
│   │   ├── register.html
│   │   ├── register.css
│   │   └── register.js
│   ├── verification/
│   │   ├── verify.html
│   │   ├── verify.css
│   │   └── verify.js
│   └── dashboard/
│       ├── dashboard.html
│       ├── dashboard.css
│       └── dashboard.js
└── README.md
```

---

## ⚙️ Prerequisites

Make sure you have the following installed:

- Java 17+
- Maven
- MySQL
- Redis
- Any modern browser

---

## 🚀 How to Run

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/shreemansu/VELOCITY.git
cd VELOCITY
```

### 2️⃣ Set Up the Database

- Open MySQL and create a database:
```sql
CREATE DATABASE velocity_db;
```

### 3️⃣ Configure application.properties

Inside `backend/src/main/resources/`, create a file named `application.properties` and add:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/velocity_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=your_jwt_secret_key

# Redis
spring.redis.host=localhost
spring.redis.port=6379

# Email (for OTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_app_password
```

### 4️⃣ Start Redis Server

```bash
redis-server
```

### 5️⃣ Run the Backend

```bash
cd backend
./mvnw spring-boot:run
```

Backend runs on: `http://localhost:8080`

### 6️⃣ Run the Frontend

Open `frontend/login/login.html` directly in your browser or use VS Code Live Server.

---

## 🔗 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v2/car/user/registration` | Register new user |
| POST | `/api/v2/car/user/verification` | Verify OTP |
| POST | `/api/v3/car/auth/login` | Login & get JWT token |
| GET | `/api/v1/car/showAllCar` | Get all cars |
| POST | `/api/v1/car` | Add new car |
| PUT | `api/v2/car/user/{email}` | Update User |
| DELETE | `/api/v1/car/{id}` | Delete car |

---

## 👨‍💻 Author

**Shreemansu Sekhar**
- GitHub: [@shreemansu](https://github.com/shreemansu)

---

> ⚠️ `application.properties` is excluded from this repository for security reasons. Please create it manually using the template above.
