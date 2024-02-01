# Goodreads

A goodreads-like application made for a university subject.

## Tech Stack

**Template engine:** Thymeleaf

**Application and API:** Spring Boot

## Run Locally

Start the database

```bash
  docker compose up -d
```

Go to the app directory

```bash
  cd goodreads
```

Start the app

```bash
  ./gradlew bootRun
```

The app is now accessible on http://localhost:8081/.

Adminer is accessible on http://localhost:8080/.

## Running Tests

Go to the app directory

```bash
  cd goodreads
```

To run tests, run the following command

```bash
  ./gradlew test
```
