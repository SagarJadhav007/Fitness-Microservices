# 🏋️ FitX  
### *Cloud-Native Fitness Microservices Platform with Secure OAuth2 Authentication*

[![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?logo=springboot&style=for-the-badge)]()
[![React](https://img.shields.io/badge/Frontend-React-61DAFB?logo=react&style=for-the-badge)]()
[![Keycloak](https://img.shields.io/badge/Auth-Keycloak-4D4D4D?logo=keycloak&style=for-the-badge)]()
[![Docker](https://img.shields.io/badge/Containerized-Docker-2496ED?logo=docker&style=for-the-badge)]()
[![RabbitMQ](https://img.shields.io/badge/Messaging-RabbitMQ-FF6600?logo=rabbitmq&style=for-the-badge)]()

---

## 🌐 Overview

**FitX** is a cloud-native fitness tracking platform built using a distributed microservices architecture. It enables users to log activities, receive AI-driven recommendations, and manage profiles securely through a centralized API Gateway.

Designed to resemble real-world production systems, FitX demonstrates secure authentication, service discovery, event-driven communication, and containerized infrastructure in a single cohesive platform.

Instead of a monolithic backend, each domain is handled by an independent service — making the system scalable, resilient, and easy to evolve.

---

## 🧩 Microservices Architecture

| Service | Responsibility |
|----------|---------------|
| 👤 **User Service** | Manages user profiles and account data (PostgreSQL) |
| 🏃 **Activity Service** | Tracks workouts, metrics, and activity history (MongoDB) |
| 🤖 **AI Service** | Generates personalized fitness recommendations |
| 🚪 **API Gateway** | Single entry point for routing, security, and request aggregation |
| 🔎 **Eureka Server** | Service discovery for dynamic microservice registration |
| ⚙️ **Config Server** | Centralized configuration management |

---

## 🚀 Key Highlights

- 🔐 **Enterprise-Grade Authentication** — OAuth2/OIDC with PKCE using Keycloak  
- 🧭 **Service Discovery** — Dynamic registration via Netflix Eureka  
- 📡 **Event-Driven Communication** — RabbitMQ for asynchronous workflows  
- 🧱 **Polyglot Persistence** — PostgreSQL for relational data, MongoDB for activity data  
- 🌐 **API Gateway Pattern** — Centralized routing, security, and aggregation  
- 🐳 **Fully Containerized Stack** — One-command startup using Docker Compose  
- ⚡ **Scalable Design** — Independent deployment and scaling of services  

---

## 🔐 Authentication Flow

FitX uses a modern identity architecture:

1. User authenticates via Keycloak login  
2. Frontend receives OAuth2 tokens (PKCE flow)  
3. API Gateway validates JWT tokens using Keycloak public keys  
4. Gateway propagates user identity to downstream services  
5. Services process requests securely without managing credentials  

---

## 🧠 Why FitX is Unique

- 🏗️ **Production-Like Architecture:** Mirrors real enterprise backend platforms  
- 🔒 **Centralized Security Model:** Identity handled by a dedicated auth server  
- 📦 **Infrastructure as Containers:** Databases, message broker, and auth all containerized  
- ⚙️ **Loose Coupling via Messaging:** Services communicate asynchronously when needed  
- 🚀 **Deployment-Ready Design:** Can be migrated to cloud or Kubernetes with minimal changes  

---

## ⚙️ Tech Stack

- **Backend:** Java, Spring Boot, Spring Cloud (Gateway, Eureka, Config Server)  
- **Frontend:** React, TypeScript (Vite)  
- **Authentication:** Keycloak, OAuth2, OpenID Connect (PKCE)  
- **Databases:** PostgreSQL, MongoDB  
- **Messaging:** RabbitMQ  
- **DevOps:** Docker, Docker Compose  
- **Architecture:** Microservices, REST APIs, Event-Driven Design  

---

## 🐳 Local Deployment

### Prerequisites

- Docker & Docker Compose installed

---

### Run Entire System

```bash
docker compose up --build
```

## Access Services

|Component | URL |
|----------|---------------|
|API Gateway |	http://localhost:8080 |
|Frontend | http://localhost:3000 |
|Eureka Dashboard |	http://localhost:8761 |
|Keycloak Admin | http://localhost:8181 |
|RabbitMQ UI | http://localhost:15672 |

## 📸 System Flow

```bash
Client → API Gateway → Microservices → Databases / Messaging
                ↓
            Keycloak (Auth)
                ↓
           Eureka Discovery
```

## 🎯 Future Enhancements

- 📊 Real-time analytics dashboard

- 🧠 Advanced AI coaching models

- 📱 Mobile app integration

- ☁️ Cloud deployment (AWS / Kubernetes)

- 🔔 Notification service
