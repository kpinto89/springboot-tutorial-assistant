# Spring Boot Tutorial Assistant

Spring Boot + Spring AI tutorial assistant with both:
- REST APIs (`/api/tutorial/*`)
- Thymeleaf web UI (`/ui/*`)

## 1) Prerequisites

- Java 17 (configured in `pom.xml`)
- Maven 3.9+ (`mvn` in `PATH`)
- GitHub token with access to GitHub Models

Verify tools:

```powershell
java -version
mvn -v
```

## 2) Configure environment variables

Set these in the same PowerShell session where you run the app:

```powershell
$env:GITHUB_TOKEN = "your_github_token"
$env:GITHUB_MODEL = "gpt-4o-mini"
```

Configured in `src/main/resources/application.yaml`:
- `spring.ai.openai.api-key: ${GITHUB_TOKEN}`
- `spring.ai.openai.chat.options.model: ${GITHUB_MODEL:gpt-4o-mini}`
- `spring.ai.openai.chat.completions-path: /chat/completions`

## 3) Run tests

Run all tests:

```powershell
mvn clean test
```

Run only context-load test:

```powershell
mvn -Dtest=TutorialAssistantApplicationTest test
```

## 4) Start the application

```powershell
mvn spring-boot:run
```

Default URL: `http://localhost:8080`

## 5) Web UI endpoints

Open these in your browser:

- Home: `http://localhost:8080/`
- Ask: `http://localhost:8080/ui/ask`
- Roadmap: `http://localhost:8080/ui/roadmap`
- Code Review: `http://localhost:8080/ui/review`

## 6) REST API endpoints

- `GET /actuator/health`
- `GET /api/tutorial/health`
- `POST /api/tutorial/ask`
- `POST /api/tutorial/stream` (SSE)
- `GET /api/tutorial/roadmap?topic=...&level=...`
- `POST /api/tutorial/review`

### PowerShell smoke tests

```powershell
Invoke-RestMethod "http://localhost:8080/actuator/health"
Invoke-RestMethod "http://localhost:8080/api/tutorial/health"
```

```powershell
$askBody = @{
  question = "How does dependency injection work in Spring Boot?"
  context  = "Spring Boot"
  level    = "beginner"
} | ConvertTo-Json

Invoke-RestMethod `
  -Method POST `
  -Uri "http://localhost:8080/api/tutorial/ask" `
  -ContentType "application/json" `
  -Body $askBody
```

```powershell
Invoke-RestMethod "http://localhost:8080/api/tutorial/roadmap?topic=Spring%20Security&level=intermediate"
```

```powershell
$reviewBody = @{
  code = "public class Demo { public static void main(String[] args) { System.out.println(\"hi\"); } }"
  language = "Java"
} | ConvertTo-Json

Invoke-RestMethod `
  -Method POST `
  -Uri "http://localhost:8080/api/tutorial/review" `
  -ContentType "application/json" `
  -Body $reviewBody
```

## 7) Troubleshooting

- `mvn` not found:
  - Install Maven and reopen PowerShell.
- Java is not 17:
  - Install JDK 17 and set `JAVA_HOME`.
- Auth/config failures:
  - Ensure `GITHUB_TOKEN` is set in the app terminal.
- `unknown_model`:
  - Set `GITHUB_MODEL` to a model your token can access.
- `404` from `/v1/chat/completions`:
  - Keep `spring.ai.openai.chat.completions-path: /chat/completions`.
- Port conflict on `8080`:
  - Stop the conflicting process or change `server.port`.

## 8) Optional package build

```powershell
mvn clean package -DskipTests
```
