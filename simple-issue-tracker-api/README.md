# Simple Issue Tracker API

Backend REST API for tracking software issues and bugs with Kotlin and Ktor.

## Run

```powershell
java "-Dorg.gradle.appname=gradlew" -jar "..\ktor-sample\gradle\wrapper\gradle-wrapper.jar" run
```

## Test

```powershell
java "-Dorg.gradle.appname=gradlew" -jar "..\ktor-sample\gradle\wrapper\gradle-wrapper.jar" test
```

## Endpoints

- `GET /issues`
- `GET /issues/{id}`
- `POST /issues`
- `PUT /issues/{id}`
- `DELETE /issues/{id}`
- `PUT /issues/{id}/status`
- `GET /issues?status=OPEN&priority=HIGH`
