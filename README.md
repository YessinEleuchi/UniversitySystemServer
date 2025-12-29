<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB"/>
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf"/>
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security"/>
</p>

# 🎓 UniFlow - University Management System

> A comprehensive, modern university management platform built with **Spring Boot 3** and **MongoDB**, designed to streamline academic operations, course management, and student-teacher interactions.

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Modules](#-modules)
- [Getting Started](#-getting-started)
- [Project Structure](#-project-structure)
- [API Endpoints](#-api-endpoints)
- [Screenshots](#-screenshots)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

**UniFlow** is a full-stack university management system that simplifies the daily operations of educational institutions. It provides a centralized platform for managing students, teachers, courses, schedules, assessments, and notifications.

### Key Highlights

✅ **Role-Based Access Control** - Secure authentication with distinct roles for Students, Teachers, and Administrators  
✅ **Modular Architecture** - Clean separation of concerns with domain-driven design  
✅ **Real-Time Notifications** - Email notifications for account updates and academic events  
✅ **Responsive Admin Dashboard** - Intuitive UI for managing all academic entities  

---

## ✨ Features

### 👥 People Management
- **Student Management** - Registration, profile management, and academic tracking
- **Teacher Management** - Faculty profiles with subject assignments
- **User Accounts** - Secure authentication with role-based permissions
- **Automated Password Generation** - Secure random password generation for new accounts

### 📚 Academic Management
- **Course Management** - Create and organize courses by cycles and levels
- **Speciality Management** - Define academic specializations and tracks
- **Subject Management** - Manage subjects with credit hours and coefficients
- **Semester Management** - Academic calendar organization
- **Level & Cycle Configuration** - Hierarchical academic structure (License, Master, etc.)

### 🏫 Teaching Management
- **Class Groups** - Organize students into manageable class groups
- **Course Instances** - Assign teachers to specific course sections
- **Session Scheduling** - Manage lecture, TD, and TP sessions
- **Room Management** - Allocate rooms (Amphitheater, Labs, Classrooms)
- **Course Resources** - Upload and share educational materials

### 📊 Assessment System
- **Evaluation Management** - Create exams, quizzes, and continuous assessments
- **Grade Recording** - Track student performance with detailed grading
- **Assessment Types** - Support for CC, Exams, Projects, and more

### 📧 Notification System
- **Email Integration** - SMTP-based email notifications via Gmail
- **Account Notifications** - Welcome emails with credentials for new users
- **Template-Based Emails** - Professional HTML email templates with Thymeleaf

---

## 🏗 Architecture

UniFlow follows a **Clean Layered Architecture** pattern, ensuring maintainability and scalability:

```
┌─────────────────────────────────────────────────────────┐
│                   Presentation Layer                     │
│              (Controllers, REST APIs, Views)             │
├─────────────────────────────────────────────────────────┤
│                    Service Layer                         │
│              (Business Logic, Use Cases)                 │
├─────────────────────────────────────────────────────────┤
│                   Repository Layer                       │
│              (Data Access, MongoDB Queries)              │
├─────────────────────────────────────────────────────────┤
│                    Domain Layer                          │
│              (Entities, Value Objects, Enums)            │
└─────────────────────────────────────────────────────────┘
```

Each module (Academic, People, Teaching, Assessment, Notification) follows this layered structure for consistent organization.

---

## 🛠 Tech Stack

| Category | Technology |
|----------|------------|
| **Backend Framework** | Spring Boot 3.5.7 |
| **Database** | MongoDB |
| **Security** | Spring Security |
| **Template Engine** | Thymeleaf |
| **Email** | Spring Mail (Gmail SMTP) |
| **Build Tool** | Maven |
| **Java Version** | Java 17 |
| **Code Generation** | Lombok |

---

## 📦 Modules

### 1. Academic Module (`com.eduflow.academic`)
Manages the core academic structure of the university.

| Entity | Description |
|--------|-------------|
| `Course` | Academic courses offered |
| `Cycle` | Academic cycles (License, Master, Doctorate) |
| `Level` | Study levels within cycles (L1, L2, L3, M1, M2) |
| `Semester` | Academic semesters (S1-S6, etc.) |
| `Speciality` | Academic specializations |
| `Subject` | Individual subjects within courses |

### 2. People Module (`com.eduflow.people`)
Handles user and personnel management.

| Entity | Description |
|--------|-------------|
| `Student` | Student profiles and academic info |
| `Teacher` | Faculty member profiles |
| `UserAccount` | Authentication and authorization |
| `Role` | User roles (ADMIN, TEACHER, STUDENT) |

### 3. Teaching Module (`com.eduflow.teaching`)
Manages the teaching and scheduling aspects.

| Entity | Description |
|--------|-------------|
| `ClassGroup` | Student groups for courses |
| `CourseInstance` | Specific course offerings per semester |
| `Session` | Scheduled teaching sessions |
| `Room` | Physical classrooms and labs |
| `CourseResource` | Educational materials and resources |

### 4. Assessment Module (`com.eduflow.assessment`)
Handles student evaluations and grading.

| Entity | Description |
|--------|-------------|
| `Evaluation` | Exams, quizzes, and assessments |
| `Grade` | Student grades and scores |
| `AssessmentType` | Type classification (Exam, CC, Project) |

### 5. Notification Module (`com.eduflow.notification`)
Manages communication and notifications.

| Component | Description |
|-----------|-------------|
| `AccountEmailDispatcher` | Email notification service |
| `AccountMailContext` | Email template context |

---

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **MongoDB** 6.0+ running locally or remotely
- **Maven** 3.8+
- **Gmail Account** (for email notifications)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/YessinEleuchi/UniFlow.git
   cd UniFlow
   ```

2. **Configure MongoDB**
   
   Ensure MongoDB is running on `localhost:27017`. The application will create a database named `uniflow`.

3. **Configure Email (Optional)**
   
   Update `src/main/resources/application.properties` with your Gmail credentials:
   ```properties
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   ```
   > ⚠️ Use [Gmail App Passwords](https://support.google.com/accounts/answer/185833) for security.

4. **Build the project**
   ```bash
   ./mvnw clean install
   ```

5. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

6. **Access the application**
   
   Open your browser and navigate to: `http://localhost:8080`

---

## 📁 Project Structure

```
UniFlow/
├── src/
│   ├── main/
│   │   ├── java/com/eduflow/
│   │   │   ├── academic/          # Academic management module
│   │   │   │   ├── domain/        # Entities (Course, Level, Subject, etc.)
│   │   │   │   ├── presentation/  # REST Controllers
│   │   │   │   ├── repo/          # MongoDB Repositories
│   │   │   │   └── service/       # Business Logic
│   │   │   ├── assessment/        # Grading & evaluation module
│   │   │   ├── notification/      # Email notification services
│   │   │   ├── people/            # User & personnel management
│   │   │   ├── security/          # Spring Security configuration
│   │   │   └── teaching/          # Course delivery & scheduling
│   │   └── resources/
│   │       ├── templates/         # Thymeleaf HTML templates
│   │       │   └── admin/         # Admin dashboard views
│   │       └── application.properties
│   └── test/                      # Unit & integration tests
├── admin/                         # Admin panel assets
├── pom.xml                        # Maven configuration
└── README.md
```

---

## 🔌 API Endpoints

### Academic Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/courses` | List all courses |
| POST | `/api/courses` | Create a new course |
| GET | `/api/subjects` | List all subjects |
| GET | `/api/levels` | List all academic levels |
| GET | `/api/semesters` | List all semesters |
| GET | `/api/specialities` | List all specialities |

### People Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/students` | List all students |
| POST | `/api/students` | Register a new student |
| GET | `/api/teachers` | List all teachers |
| PUT | `/api/users/{id}` | Update user profile |

### Teaching Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/class-groups` | List all class groups |
| GET | `/api/sessions` | List all sessions |
| GET | `/api/rooms` | List all rooms |

---

## 🖼 Screenshots

> 📸 *Screenshots coming soon! The admin dashboard provides intuitive management interfaces for all academic operations.*

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Yessin Eleuchi**  
Final Year Software Engineering Student

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/YessinEleuchi)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/your-profile)

---

<p align="center">
  <sub>⭐ Star this repository if you found it helpful!</sub>
</p>
