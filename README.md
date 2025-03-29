# Pass Generator Application

A Spring Boot application for generating access passes with QR codes, featuring batch processing and priority queue management.

## Features

- Batch generation of access passes
- QR code generation for each pass
- VIP priority processing
- CSV file input support
- RESTful API with Swagger UI
- Spring Batch implementation for scalable processing

## Technologies

- Java 17
- Spring Boot 3
- Spring Batch
- Spring Data JPA
- Lombok
- H2 Database
- Swagger UI
- ZXing (QR Code generation)
- Maven

## Getting Started

### Installation

1. Clone the repository
2. Navigate to the project directory
3. Build the project:
```bash
mvn clean install
```

### Running the Application
Start the application using:

```bash
mvn spring-boot:run
```
The application will be available at http://localhost:8080

### API Documentation
Access Swagger UI at: http://localhost:8080/swagger-ui.html

## API Endpoints
### Generate Passes

```txt
POST /api/passes/generate
```

Initiates the pass generation process by reading data from the CSV file.

### Search Pass
```txt
GET /api/passes/search?nom={nom}&prenom={prenom}&dateNaissance={dateNaissance}
```

Search for a specific pass using name and birth date.

## CSV File Format
Place your CSV file at src/main/resources/passes.csv with the following format:

```txt
nom,prenom,dateNaissance,statusVIP
Dupont,Jean,1990-01-01,true
Martin,Sophie,1985-03-15,false
```

## Database
The application uses H2 in-memory database. Access the H2 console at: http://localhost:8080/h2-console

Default credentials:

- JDBC URL: jdbc:h2:mem:passdb
- Username: sa
- Password: (empty)