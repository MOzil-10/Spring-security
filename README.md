# Spring Security Application

## Overview
This application is designed to help users securely manage authentication and authorization processes. Whether you're signing up for an account or logging in, our system ensures that your information is handled with the highest level of security.

## Features
- **User Registration**: Allows new users to create an account.
- **User Login**: Provides a secure way to log in with your credentials.
- **Token Management**: Issues a secure token upon login that is used to access various features of the application.
- **Dockerized Setup**: Easily deploy and manage the application using Docker containers.

## Database
This application uses PostgreSQL as the database for storing user data and managing authentication. The database schema is initialized with a script provided in the project.

## Getting Started
To use the Security System, follow these simple steps:

## Prerequisites
Java 17
Maven
Docker and Docker Compose

## Sign Up
Go to the registration page.
Enter your details (such as email and password).
Submit the form to create your account.

## Log In
Go to the login page.
Enter your email and password.
Click "Login" to access your account.

## Understanding the Token
After logging in, you will receive a security token.
This token verifies your identity and grants you access to secured areas of the application.
The token has an expiration time, ensuring that it is valid only for a specific duration.

## Expiration Time
The token will expire after a certain period for enhanced security.
Once expired, you'll need to log in again to receive a new token.

## Dockerization
We have containerized the application using Docker to simplify deployment and ensure consistency across different environments.
