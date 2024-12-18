# **Calculator API Project**

A **RESTful calculator API** with two microservices (**REST** and **Calculator**) communicating via **Apache Kafka**. The infrastructure is monitored using **Grafana**, **Prometheus**, and **Loki**.

## **Prerequisites**

- Java 17
- Maven
- Docker and Docker Compose
- Monitoring tools:
    - Grafana
    - Loki
    - Promtail

---

## **Getting Started**

### 1. Clone the repository

```bash
git clone https://github.com/gcmelo76/Calculator-Project.git
cd Calculator-Project
```

### 2. Start the services with Docker Compose

```bash
docker-compose up --build
```

### 3. Stop and Clean Services (Optional)

```bash
docker-compose down -v
```

- **REST Service** available at: `http://localhost:8084`
- **Calculator Service** available at: `http://localhost:8083`
- **Swagger UI**: `http://localhost:8084/swagger-ui/index.html`

---

## **API Documentation**

### Swagger UI

- Access **Swagger UI** for testing endpoints: [http://localhost:8084/swagger-ui/index.html](http://localhost:8084/swagger-ui/index.html)

### Quick Reference

All endpoints accept **POST** requests with query parameters `a` and `b` as decimal numbers.

#### Endpoints

- **Addition**: `http://localhost:8084/api/calculator/add?a=10&b=5`
- **Subtraction**: `http://localhost:8084/api/calculator/subtract?a=10&b=5`
- **Multiplication**: `http://localhost:8084/api/calculator/multiply?a=10&b=5`
- **Division**: `http://localhost:8084/api/calculator/divide?a=10&b=5`

Example response:
```json
{
    "result": 15
}
```

---

## **Project Structure**

- **Rest/**: REST API service (port `8084`) exposing the API endpoints.
- **Calculator/**: Calculation service (port `8083`) responsible for operations.
- **promtail/**: **Promtail** configuration for structured log collection.
- **docs/**: API documentation.
- **docker-compose.yml**: Orchestrates the services using Docker.

---

## **Monitoring & Observability**

### **Grafana**
**Grafana dashboard** is available at:

- [http://localhost:3000](http://localhost:3000)

Default credentials:
- **Username**: `admin`
- **Password**: `admin`

### **Prometheus**
- Endpoint: [http://localhost:9090](http://localhost:9090)

**Prometheus** collects metrics from the `Calculator` and `REST` services.

### **Loki and Promtail**
**Structured logs** are sent from **Promtail** to **Loki** and can be viewed in Grafana.

#### Logs Include:
- **timestamp**: The time the log was generated.
- **app_name**: The name of the application generating the log.
- **loglevel**: The log level (e.g., INFO, DEBUG, ERROR).
- **requestId**: Unique identifier for tracing a specific request.
- **message**: The log message.
- **exception**: Stack trace details if an error occurred.

---

## **How It Works**

1. **REST Service** receives HTTP requests and publishes messages to the Kafka topic `calculator-requests`.
2. **Calculator Service** consumes messages from Kafka, processes the calculation, and publishes the result to the `calculator-responses` topic.
3. The **REST Service** consumes the result from Kafka and returns the response to the client.
4. **Logs** are structured, sent to **Loki** via **Promtail**, and visualized in **Grafana**.
5. **Metrics** are exposed via `/actuator/prometheus` and collected by **Prometheus**.

---

## **Troubleshooting**

### Common Issues

1. **Port Conflicts**
    - Ensure the following ports are free: `8083`, `8084`, `9092`, `3000`, `3100`.
    - Verify no other services are running on these ports:
      ```bash
      docker ps
      ```

2. **Check Service Logs**
    - Follow the service logs with:
      ```bash
      docker-compose logs -f
      ```

3. **Kafka Startup Issues**
    - Ensure **Zookeeper** is running.
    - Restart Kafka services:
      ```bash
      docker-compose restart kafka zookeeper
      ```

4. **Logs Missing in Grafana**
    - Verify that **Promtail** is running:
      ```bash
      docker-compose logs promtail
      ```
    - Check **logback-spring.xml** configuration:
      ```xml
      <customFields>{"app_name":"${APP_NAME}"}</customFields>
      ```

---

## **Technologies Used**

- **Spring Boot 3** (RESTful API)
- **Apache Kafka** (Message broker for microservices communication)
- **Docker** (Containerization and orchestration with Docker Compose)
- **Grafana, Prometheus, Loki, and Promtail** (Monitoring and structured logging)
- **Maven** (Build tool and dependency management)
- **OpenAPI** (Swagger for API documentation)