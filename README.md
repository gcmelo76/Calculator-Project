# Calculator API Project

A RESTful calculator API with two microservices (REST and Calculator) communicating via Apache Kafka.

## Prerequisites

- Java 17
- Maven
- Docker and Docker Compose

## Getting Started

1. Clone the repository
```bash
git clone https://github.com/gcmelo76/Calculator-Project.git
cd Calculator-Project
```

2. Build the project
```bash
mvn clean package
```

3. Start the services
```bash
docker-compose up --build
```

The services will be available after Kafka and the applications are fully initialized.

## API Endpoints

All endpoints accept GET requests with query parameters `a` and `b` as decimal numbers.

- Addition: `http://localhost:8084/api/calculator/add?a=10&b=5`
- Subtraction: `http://localhost:8084/api/calculator/subtract?a=10&b=5`
- Multiplication: `http://localhost:8084/api/calculator/multiply?a=10&b=5`
- Division: `http://localhost:8084/api/calculator/divide?a=10&b=5`

Example response:
```json
{
    "result": 15
}
```

## Project Structure

- `Rest/`: REST API service (port 8084)
- `Calculator/`: Calculation service (port 8083)
- Both services communicate through Kafka topics

## Troubleshooting

If you encounter any issues:

1. Ensure all ports (8083, 8084, 9092) are available
2. Check if Docker services are running: `docker ps`
3. View logs: `docker-compose logs -f`

## Technologies

- Spring Boot 3
- Apache Kafka
- Docker
- Maven