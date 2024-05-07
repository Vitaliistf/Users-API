# Users API

## Project description

> This repository contains a RESTful API responsible for the resource named Users, which is
> a solution for practical test assignment from Clear Solutions.

## Table of Contents

- [Requirements](#requirements)
- [Technologies Used](#technologies-used)
- [Technical Details](#technical-details)
- [Project Structure](#project-structure)
- [Testing](#testing)
- [Getting Started](#getting-started)

## Requirements

Requirements for this assignment can be found [here](https://docs.google.com/document/d/1LosRgr72sJYcNumbZKET7uiIJ3Un_ORl25Psn1Dd9hw/edit).

## Technologies used

- Java 21
- Spring Boot 3.2.5 (Web, Data)
- MySQL
- Lombok
- Maven
- JUnit 5
- Mockito
- MapStruct
- Liquibase

## Technical details

There are both JavaDoc and Swagger documentations provided. You may refer to them for additional info.

## Project structure

The project has a three-tier Architecture:

| Layer                                 | Responsibilities                                                  | 
|---------------------------------------|-------------------------------------------------------------------|
| **Presentation layer (Controller)**   | Controllers accept requests and send prepared response.           |
| **Application logic layer (Service)** | Provide logic to operate on the data from the DB and the client.  |
| **Data access layer (Repository)**    | Represents a connection between the database and the application. |

## Testing
The application business logic (Service) and presentation layer (Controller) are tested using JUnit and Mockito.

## Getting Started

#### To get started with the project, follow these steps:
> Note that soon as you start this project, the database will be created on its own. Table will be created by Liquibase.  
> No additional actions needed.

- Clone this repository;
- Set your properties in [application.properties](src/main/resources/application.properties);
- Build the project `mvn clean package`;
- Press `run`;