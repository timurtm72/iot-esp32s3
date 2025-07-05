# IoT ESP32 Backend (Spring Boot 3 / Java 17)

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15%2B-blue)
![MapStruct](https://img.shields.io/badge/MapStruct-1.5.5-yellow)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.1-lightgrey)

Современный бэкенд для **ESP32-устройств** с управлением LED-лентами и сбором показаний t°/RH. Построен на **Spring Boot 3** и использует лучшие практики Java 17.

> Проект учебный, но изначально строится по production-стандартам: слоистая архитектура, DTO-слой, soft-delete, кеширование, плановый data-retention, Swagger-документация, Actuator-метрики.

---

## 📑 Содержание

1. [Ключевые возможности](#ключевые-возможности)
2. [Архитектура и пакеты](#архитектура-и-пакеты)
3. [Модель данных](#модель-данных)
4. [REST API](#rest-api)
5. [Планировщик и ретеншн](#планировщик-и-ретеншн)
6. [Конфигурация](#конфигурация)
7. [Сборка и запуск](#сборка-и-запуск)
8. [Дальнейшие планы](#дальнейшие-планы)

---

## Ключевые возможности

* CRUD для **устройств**, **пользователей** и **телеметрии** (LED RGB, температура, влажность).
* `@Embedded` классы **`Location`** и **`WiFiParameters`** вместо строковых полей.
* Перечисление **`DeviceStatus`**: `ONLINE / OFFLINE / UNKNOWN`.
* Поиск устройств по городу (`location.city`).
* Bean-Validation в каждом DTO.
* Soft-delete через колонку `removed_at` + аудит `created_at` / `modified_at`.
* Кеширование частых запросов (`@Cacheable`).
* **Data Cleanup Scheduler** — ежедневное удаление устаревшей телеметрии.
* **OpenAPI 3.1** + Swagger UI (`/swagger-ui/index.html`).
* Spring Boot **Actuator** (`/actuator/**`).

---

## Архитектура и пакеты

```
config/       # CORS, Scheduler и прочая инфраструктура
controller/   # REST-контроллеры (Spring MVC + OpenAPI)
dto/          # Транспортные объекты + Bean Validation
mapper/       # MapStruct-мапперы DTO ⇄ Entity
model/        # JPA-сущности и embedded-типы
repository/   # Spring Data JPA
service/      # Контракты бизнес-логики
serviceimpl/  # Реализации сервисов
scheduler/    # @Scheduled задачи (data cleanup)
validation/   # Глобальная обработка ошибок валидации
```

Зависимости направлены сверху вниз (Controller → Service → Repository), что упрощает тестирование и поддержку.

---

## Модель данных

```
User (1)───(N) Device ──┬─ (N) LedStripData
                       ├─ (N) TempAndHumidityData
                       ├─ Location      (embedded)
                       └─ WiFiParameters(embedded)
```

| Сущность | Основные поля |
|----------|---------------|
| **User** | `username`, `email`, `password`, `role`, `active`, `lastLogin` |
| **Device** | `name`, `description`, `status`, `location`, `wifiParams`, `owner` |
| **Location** | `country`, `city`, `street`, `house`, `apartment`, `room` |
| **WiFiParameters** | `ssid`, `password`, `ipAddress`, `macAddress` |
| **LedStripData** | `red`, `green`, `blue`, `brightness`, `timestamp` |
| **TempAndHumidityData** | `temperature`, `humidity`, `timestamp` |

Все сущности наследуют `BaseEntity` с полями аудита и lifecycle-колбэками (`@PrePersist` etc.).

---

## REST API

Базовый URL: `http://localhost:8081/api`

Только основные эндпоинты (подробности — в Swagger UI):

| Метод | URI | Описание |
|-------|-----|----------|
| GET | `/devices` | Все активные устройства |
| GET | `/devices/{id}` | Устройство по ID |
| POST | `/devices` | Создать устройство |
| PUT | `/devices/{id}` | Обновить |
| DELETE | `/devices/{id}` | Soft-delete |
| GET | `/devices/created-after/{isoDateTime}` | Созданные после даты |
| GET | `/devices/city/{city}` | Устройства по городу |
|---|---|---|
| GET | `/led-strip-data` | Вся LED-телеметрия |
| POST | `/led-strip-data` | Новая запись |
| GET | `/led-strip-data/{id}` | Запись по ID |
| GET | `/led-strip-data/device/{deviceId}/range?start=ISO&end=ISO` | Диапазон по устройству |
|---|---|---|
| GET | `/temp-humidity-data` | Вся t°/RH телеметрия |
| POST | `/temp-humidity-data` | Новая запись |

Пользовательские эндпоинты аналогичны (`/users/**`).

---

## Планировщик и ретеншн

Компоненты:

* **`DataCleanupServiceImpl`** — удаляет записи старше заданного порога.
* **`DataCleanupScheduler`** (`@Scheduled`) — триггер службы по Cron.

Настройки в `application.properties`:

```properties
# Кол-во дней хранения телеметрии
data.retention-days=30
# Ежедневно в 03:00 (sec min hr day month dow)
data.cleanup.cron=0 0 3 * * *
```

Для изменения достаточно поменять свойства и перезапустить приложение.

---

## Конфигурация

| Свойство | Значение | Описание |
|----------|----------|----------|
| `server.port` | `8081` | Порт HTTP |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/iot` | Подключение к PostgreSQL |
| `spring.jpa.hibernate.ddl-auto` | `create-drop` | Авто-DDL (dev) |
| `spring.jpa.show-sql` | `true` | Логирование запросов |
| `data.retention-days` | `30` | Сколько дней хранить телеметрию |
| `data.cleanup.cron` | `0 0 3 * * *` | Cron-выражение планировщика |

Полный список смотрите в файле конфигурации.

---

## Сборка и запуск

```bash
# Клонировать репозиторий
$ git clone https://github.com/you/iot-esp32s3.git && cd iot-esp32s3

# Сборка (Linux/Mac)
$ ./mvnw clean package
# или Windows
> mvnw.cmd clean package

# Запуск собранного JAR
$ java -jar target/iot-java-esp32s3-0.0.1-SNAPSHOT.jar
```

Альтернативно:

```bash
./mvnw spring-boot:run
```

Swagger: <http://localhost:8081/swagger-ui/index.html>  
Actuator Health: <http://localhost:8081/actuator/health>

### Предпосылки

* **Java 17**+
* **PostgreSQL 12**+
* **Maven 3.8**+

---

## Дальнейшие планы

- [ ] JWT / Spring Security
- [ ] WebSocket для realtime push
- [ ] Docker Compose (PostgreSQL + backend)
- [ ] CI/CD (GitHub Actions)
- [ ] Интеграционные тесты

---

© 2024 Timur — MIT License 