# Документация проекта

## Описание

Проект состоит из **двух микросервисов**:

- **AuthService** — отвечает за регистрацию пользователей, вход в систему и проверку JWT-токенов. Данные хранятся в базе `authdb`.
- **OrderService** — позволяет создавать и просматривать заказы между станциями, проверяя токены через AuthService. Данные хранятся в базе `orderdb`.

Сервисы общаются по HTTP и запускаются совместно через Docker Compose.

---

## AuthService

**Основные возможности**:
- Регистрация пользователя (`POST /api/register`)
- Аутентификация и выдача JWT-токена (`POST /api/login`)
- Проверка валидности токена (`GET /api/validate?token=...`)
- Получение данных о пользователе (`GET /api/user`)

**Пример запроса**:
```http
POST /api/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "secret"
}
```
**Ответ**:
```json
"jwt-token-here"
```

---

## OrderService

**Основные возможности**:
- Создание и получение заказов (требуется действительный JWT-токен)
- Управление станциями: получение списка станций или информации по конкретной
- Планировщик (`@Scheduled`) меняет статус заказа через время — имитация обработки
- Проверка токена через AuthService (REST-запрос)

**Пример запроса**:
```http
POST /api/orders
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "userId": 1,
  "fromStation": { "id": 1 },
  "toStation": { "id": 2 }
}
```
**Ответ**:
```json
{
  "id": 10,
  "userId": 1,
  "fromStation": { "id": 1, "station": "Station A" },
  "toStation": { "id": 2, "station": "Station B" },
  "status": 1,
  "created": "2025-03-27T15:30:00"
}
```

---

## Используемые технологии

- **Java 17**
- **Spring Boot** (Web, Security, Data JPA, Validation, Scheduling)
- **PostgreSQL**
- **Docker**, **Docker Compose**
- **PgAdmin** (для управления БД)
- **JWT** (библиотека `jjwt`)
- **JUnit**, **Mockito** (тестирование)

---

## Быстрый запуск

1. Установите [Docker](https://www.docker.com/) и [Docker Compose](https://docs.docker.com/compose/).
2. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/username/project.git
   cd project
   ```
3. Запустите:
   ```bash
   docker-compose up
   ```
   Поднимутся контейнеры с PostgreSQL, PgAdmin, AuthService и OrderService.

4. PgAdmin доступен на [http://localhost:8081](http://localhost:8081)
    - Email: `admin@example.com`
    - Пароль: `admin`

5. Добавьте сервера `authdb` и `orderdb` в PgAdmin (если нужно).
6. Для примера заполните таблицу `stations` в базе `orderdb`:
   ```sql
   INSERT INTO stations (id, station) VALUES
   (1, 'Station A'),
   (2, 'Station B'),
   (3, 'Station C'),
   (4, 'Station D'),
   (5, 'Station E');
   ```

**AuthService** будет доступен на порту `8082`, **OrderService** — на `8083`.

---

