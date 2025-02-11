# Library Management System using Java (Spring Boot) and Microservices Architecture

This project implements a **Library Management System** using **Java (Spring Boot)** with a **Microservices Architecture**.  
Here is the link to the **Monolithic Architecture** version of this project: [LibraryManagement](https://github.com/vdt104/LibraryManagement)

## Overview
This project is designed to manage various library operations, including **user management, document management, borrowing requests, and notifications**.  
The system adopts a **Microservices Architecture**, which divides functionalities into small, independent services, making it easier to manage and scale.

## Core Services
- **API Gateway**: Acts as the entry point of the system, routing user requests to the corresponding services.
- **Config Server**: Manages centralized configurations for all services.
- **Document Service**: Manages books and other library documents.
- **Notification Service**: Handles notifications.
- **Reader Request Service**: Manages book borrowing requests from users.
- **Reader Service**: Manages reader information.
- **Service Registry**: Facilitates service registration and discovery.
- **User Service**: Manages user information and activities.

## Technologies Used
- **Java (Spring Boot)**: The primary programming language and framework for developing the services.
- **Kafka**: Message broker for inter-service communication.
- **API Gateway**: Manages and routes user requests.
- **Microservices Architecture**: Divides the system into small, independent services for better scalability and maintainability.
- **Docker**: Containerize services for easy deployment and management.
- **Database**: Use MySQL and MongoDB to store data.

## Database Schema
Here is the database schema for the project (from the Monolithic Architecture version):
![LM](https://github.com/user-attachments/assets/6e955a11-c252-4a71-ac4d-c57d7ccca6d9)
