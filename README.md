# Spring Boot Kafka REST API (Advanced)

This is a Spring Boot application demonstrating advanced event-driven patterns using Apache Kafka, including structured JSON data and fault-tolerant processing.

## 🚀 Tech Stack
- **Java 21**
- **Spring Boot 3.3.5**
- **Apache Kafka** (KRaft Mode)
- **Docker & Docker Compose**
- **SpringDoc OpenAPI (Swagger)**

## 🛠️ Getting Started

### 1. Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.
- [JDK 21](https://adoptium.net/) installed.
- Maven (provided via `./mvnw` wrapper).

### 2. Infrastructure Setup
Start the Kafka broker and the Kafka UI using Docker Compose:

```bash
docker compose up -d
```

This will start:
- **Kafka Broker:** Accessible internally via port `9092` and externally via `9094`.
- **Kafka UI:** Accessible at [http://localhost:8081](http://localhost:8081).

### 3. Run the Application
Start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

## 📖 API Documentation

The application includes a Swagger UI for easy testing of the endpoints.
👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

### Endpoints

#### 1. Produce a Kafka Event
**Endpoint:** `POST /kafka/publish`
**Query Param:** `message` (String)
**Action:** Wraps the message in a `UserMessage` JSON object and sends it to `my-topic`.

Example using `curl`:
```bash
curl -X POST "http://localhost:8080/kafka/publish?message=HelloKafka"
```

#### 2. Get Successfully Processed Messages
**Endpoint:** `GET /kafka/messages`
**Action:** Returns a list of all `UserMessage` objects successfully consumed.

#### 3. Get Failed Messages (DLT)
**Endpoint:** `GET /kafka/messages/dlt`
**Action:** Returns a list of all `UserMessage` objects that failed all retry attempts and were moved to the Dead Letter Topic.

## 🛡️ Advanced Patterns Implemented

### 📦 Structured Data (JSON)
Instead of raw strings, the application uses a `UserMessage` DTO:
```json
{
  "id": "uuid",
  "content": "message text",
  "timestamp": 123456789
}
```
 configured via `JsonSerializer` and `JsonDeserializer`.

### 🔄 Retry & Dead Letter Topic (DLT)
The consumer implements a non-blocking retry mechanism:
- **Retry Policy:** 3 attempts with exponential backoff (1s, 2s, 4s).
- **Failure Trigger:** Any message containing the word `"fail"` will trigger a simulated processing error.
- **DLT Flow:** After all retries are exhausted, the message is automatically routed to the `my-topic-dlt` topic.

## 🔍 Monitoring & Verification

### 1. Application Logs
Check the console to observe the retry cycle:
- `Produced: ...`
- `Consumed: ...`
- `Simulating failure...` $\rightarrow$ (Retry attempts) $\rightarrow$ `Message sent to DLT...`

### 2. Kafka UI
Visit **[http://localhost:8081](http://localhost:8081)** to:
- View the `my-topic` and `my-topic-dlt` topics.
- Inspect the JSON payload of the messages.
- Monitor the `my-group` consumer group.

## 🧹 Cleanup
To stop and remove the Kafka containers:
```bash
docker compose down
```
