# Spring Boot Tutorial Assistant - Testing Guide

This guide explains how to test the application locally on Windows PowerShell.

## 1) Prerequisites

- Java 17 (project is configured for Java 17 in `pom.xml`)
- Maven 3.9+ (`mvn` available in `PATH`)
- GitHub token with access to GitHub Models (for AI endpoints)

Verify tools:

```powershell
java -version
mvn -v
```

## 2) Configure environment

From project root, set the token for your current PowerShell session:

```powershell
$env:GITHUB_TOKEN = "your_github_token"
$env:GITHUB_MODEL = "gpt-4o-mini"
```

The app reads this from `src/main/resources/application.yaml` via:
`spring.ai.openai.api-key: ${GITHUB_TOKEN}`, `spring.ai.openai.chat.options.model: ${GITHUB_MODEL:gpt-4o-mini}`, and `spring.ai.openai.chat.completions-path: /chat/completions`.

## 3) Run automated tests

Run all tests:

```powershell
mvn clean test
```

Run only the context-load test:

```powershell
mvn -Dtest=TutorialAssistantApplicationTest test
```

Current test coverage includes:
- `src/test/java/com/demo/TutorialAssistantApplicationTest.java`
- Verifies Spring context starts successfully.

## 4) Start the app for API smoke tests

```powershell
mvn spring-boot:run
```

App defaults to port `8080`.

## 5) Smoke test endpoints (PowerShell)

Open a second terminal and run:

### Health checks

```powershell
Invoke-RestMethod "http://localhost:8080/actuator/health"
Invoke-RestMethod "http://localhost:8080/api/tutorial/health"
```

### Ask endpoint

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

### Roadmap endpoint

```powershell
Invoke-RestMethod "http://localhost:8080/api/tutorial/roadmap?topic=Spring%20Security&level=intermediate"
```

### Code review endpoint

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

### Stream endpoint (SSE)

```powershell
$streamBody = @{
  question = "Explain Spring Boot auto-configuration"
  context  = "Spring Boot"
  level    = "intermediate"
} | ConvertTo-Json

Invoke-WebRequest `
  -Method POST `
  -Uri "http://localhost:8080/api/tutorial/stream" `
  -ContentType "application/json" `
  -Body $streamBody
```

## 6) Troubleshooting

- `mvn` not found:
  - Install Maven and reopen PowerShell.
- Java version is not 17:
  - Install JDK 17 and set `JAVA_HOME` to that JDK.
- API calls fail with auth/config errors:
  - Confirm `GITHUB_TOKEN` is set in the same terminal session used to run the app.
- API calls fail with `unknown_model`:
  - Set `GITHUB_MODEL` to a model your token can access (for example: `gpt-4o-mini`).
- API calls fail with `404` from `/v1/chat/completions`:
  - Confirm `spring.ai.openai.chat.completions-path` is `/chat/completions` in `src/main/resources/application.yaml`.
- Port already in use:
  - Stop the process on `8080` or change `server.port` in `src/main/resources/application.yaml`.

## 7) Optional: package without tests

```powershell
mvn clean package -DskipTests
```
