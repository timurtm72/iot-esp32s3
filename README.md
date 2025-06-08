# IoT ESP32-S3 Management System

Spring Boot приложение для управления IoT устройствами ESP32-S3 с возможностью сбора и анализа телеметрии.

## 🚀 Описание проекта

Система управления IoT устройствами ESP32-S3 предоставляет полнофункциональный REST API для:
- Управления пользователями с ролевой моделью
- Регистрации и мониторинга IoT устройств 
- Сбора и анализа данных телеметрии (температура, влажность, RGB, яркость)
- Кеширования для повышения производительности

## 📋 Технический стек

- **Java 17+** с современным синтаксисом
- **Spring Boot 3.3.1** с автоконфигурацией
- **PostgreSQL** как основная база данных
- **Spring Data JPA** / Hibernate для работы с данными
- **MapStruct** для безопасного маппинга DTO
- **Lombok** для уменьшения boilerplate кода
- **Spring Cache** для оптимизации производительности
- **Maven** для управления зависимостями

## 🏗️ Архитектура

```
├── Models (User, Device, DeviceData)
├── DTOs (UserDto, DeviceDto, DeviceDataDto)
├── Repositories (Spring Data JPA)
├── Services (бизнес-логика + кеширование)
├── Controllers (REST API)
├── Mappers (MapStruct)
└── Utils & Validation
```

## 📊 База данных

### Схема таблиц:

#### users
```sql
- id (BIGINT, PK, AUTO_INCREMENT)
- username (VARCHAR, UNIQUE, NOT NULL)
- email (VARCHAR, UNIQUE, NOT NULL)
- password (VARCHAR, NOT NULL)
- first_name (VARCHAR)
- last_name (VARCHAR)
- role (ENUM: ADMIN_ROLE, OPERATOR_ROLE, USER_ROLE)
- active (BOOLEAN, NOT NULL)
- created_at (TIMESTAMP, NOT NULL)
- modified_at (TIMESTAMP)
- last_login (TIMESTAMP)
- removed_at (TIMESTAMP)
```

#### device
```sql
- id (BIGINT, PK, AUTO_INCREMENT)
- name (VARCHAR, NOT NULL)
- description (VARCHAR)
- location (VARCHAR(500))
- owner_id (BIGINT, FK -> users.id, NOT NULL)
- created_at (TIMESTAMP, NOT NULL)
- modified_at (TIMESTAMP)
- removed_at (TIMESTAMP)
```

#### device_data
```sql
- id (BIGINT, PK, AUTO_INCREMENT)
- device_id (BIGINT, FK -> device.id, NOT NULL)
- temperature (FLOAT, NOT NULL)
- humidity (FLOAT, NOT NULL)
- red_color (INTEGER, NOT NULL)
- green_color (INTEGER, NOT NULL)
- blue_color (INTEGER, NOT NULL)
- brightness (INTEGER, NOT NULL)
- timestamp (TIMESTAMP, NOT NULL)
```

### Связи:
- **User ←→ Device**: Один пользователь может владеть множеством устройств
- **Device ←→ DeviceData**: Одно устройство может иметь множество записей телеметрии

## 🌐 API Endpoints

### 👥 Управление пользователями

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/users` | Получить список всех пользователей |
| `GET` | `/api/users/{id}` | Получить пользователя по ID |
| `POST` | `/api/users` | Создать нового пользователя |
| `PUT` | `/api/users/{id}` | Обновить пользователя |
| `DELETE` | `/api/users/{id}` | Удалить пользователя (soft delete) |
| `GET` | `/api/users/search?query={text}` | Поиск пользователей по имени/email |

### 🔌 Управление устройствами

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/devices` | Получить список всех устройств |
| `GET` | `/api/devices/{id}` | Получить устройство по ID |
| `POST` | `/api/devices` | Создать новое устройство |
| `PUT` | `/api/devices/{id}` | Обновить устройство |
| `DELETE` | `/api/devices/{id}` | Удалить устройство (soft delete) |
| `GET` | `/api/devices/owner/{ownerId}` | Получить устройства пользователя |
| `GET` | `/api/devices/owner/{ownerId}/count` | Количество устройств пользователя |
| `GET` | `/api/devices/search?query={text}` | Поиск устройств по имени/локации |

### 📊 Управление данными телеметрии

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/device-data` | Получить все данные телеметрии |
| `GET` | `/api/device-data/{id}` | Получить данные по ID |
| `POST` | `/api/device-data` | Добавить новые данные телеметрии |
| `PUT` | `/api/device-data/{id}` | Обновить данные телеметрии |
| `DELETE` | `/api/device-data/{id}` | Удалить данные телеметрии |
| `GET` | `/api/device-data/device/{deviceId}` | Получить данные устройства |
| `GET` | `/api/device-data/device/{deviceId}/latest` | Последние данные устройства |

## 📝 Примеры JSON

### Создание пользователя:
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER_ROLE"
}
```

### Создание устройства:
```json
{
  "name": "ESP32-Kitchen",
  "description": "Kitchen temperature and lighting sensor",
  "location": "Kitchen, 2nd floor",
  "ownerId": 1
}
```

### Добавление данных телеметрии:
```json
{
  "deviceId": 1,
  "temperature": 23.5,
  "humidity": 45.2,
  "redColor": 255,
  "greenColor": 128,
  "blueColor": 64,
  "brightness": 80
}
```

## ⚙️ Установка и запуск

### Требования:
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### 1. Клонирование репозитория:
```bash
git clone https://github.com/timurtm72/iot-esp32s3.git
cd iot-esp32s3
```

### 2. Настройка базы данных:
Создайте базу данных PostgreSQL:
```sql
CREATE DATABASE iot;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE iot TO postgres;
```

### 3. Конфигурация приложения:
Настройте `src/main/resources/application.properties`:
```properties
spring.application.name=iot-esp32s3
server.port=8081

# Database connection
spring.datasource.url=jdbc:postgresql://localhost:5432/iot
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 4. Сборка и запуск:
```bash
# Сборка проекта
./mvnw clean compile

# Запуск приложения
./mvnw spring-boot:run
```

### 5. Альтернативный запуск:
```bash
# Через JAR файл
./mvnw clean package
java -jar target/iot-java-esp32s3-0.0.1-SNAPSHOT.jar
```

## 🌐 Доступ к приложению

После запуска приложение будет доступно по адресу:
**http://localhost:8081**

## 🔧 Возможности системы

- ✅ **Полный CRUD** для всех сущностей
- ✅ **Ролевая модель** пользователей
- ✅ **Связанные данные** User-Device-DeviceData
- ✅ **Кеширование** для оптимизации производительности
- ✅ **Поиск и фильтрация** по различным критериям
- ✅ **Soft Delete** для безопасного удаления
- ✅ **Validation** входящих данных
- ✅ **Timestamp tracking** для аудита
- ✅ **MapStruct маппинг** DTO ↔ Entity

## 📈 Производительность

- **Spring Cache** для кеширования часто используемых данных
- **Connection Pooling** через HikariCP
- **Lazy Loading** для связанных сущностей
- **Derived Query Methods** для оптимизированных запросов

## 🛠️ Технические особенности

### MapStruct Configuration:
```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
```

### Caching Strategy:
```java
@Cacheable(value = "users", key = "#id")
public UserDto findById(Long id);

@CacheEvict(value = "users", key = "#id")
public void deleteById(Long id);
```

### Repository Pattern:
```java
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByOwnerIdAndRemovedAtIsNull(Long ownerId);
    Long countByOwnerIdAndRemovedAtIsNull(Long ownerId);
}
```

## 🔮 Планы развития

- [ ] Spring Security интеграция
- [ ] JWT Authentication
- [ ] WebSocket для real-time данных
- [ ] Swagger/OpenAPI документация
- [ ] Unit & Integration тесты
- [ ] Docker контейнеризация
- [ ] Monitoring с Actuator
- [ ] Data visualization dashboard

## 👨‍💻 Автор

**Timur** - [GitHub Profile](https://github.com/timurtm72)

## 📄 Лицензия

Этот проект разработан для учебных целей. 