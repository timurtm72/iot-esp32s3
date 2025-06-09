# IoT ESP32-S3 Management System

Spring Boot приложение для управления специализированными IoT устройствами ESP32-S3 с возможностью сбора и анализа телеметрии.

## 🚀 Описание проекта

Система управления IoT устройствами ESP32-S3 предоставляет полнофункциональный REST API для:
- Управления пользователями с ролевой моделью
- Управления специализированными устройствами: датчики температуры/влажности и LED ленты
- Сбора и анализа телеметрических данных (температура, влажность, RGB, яркость)
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
├── Models (User, TempAndHumidity, LedStrip, TempAndHumidityData, LedStripData)
├── DTOs (UserDto, TempAndHumidityDto, LedStripDto, *DataDto)
├── Repositories (Spring Data JPA с интеллектуальными запросами)
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

#### temp_and_humidity (Датчики температуры и влажности)
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

#### led_strip (LED ленты)
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

#### temp_and_humidity_data (Данные датчиков температуры/влажности)
```sql
- id (BIGINT, PK, AUTO_INCREMENT)
- device_id (BIGINT, FK -> temp_and_humidity.id, NOT NULL)
- temperature (FLOAT, NOT NULL)
- humidity (FLOAT, NOT NULL)
- timestamp (TIMESTAMP, NOT NULL)
```

#### led_strip_data (Данные LED лент)
```sql
- id (BIGINT, PK, AUTO_INCREMENT)
- device_id (BIGINT, FK -> led_strip.id, NOT NULL)
- red_color (INTEGER, NOT NULL)
- green_color (INTEGER, NOT NULL)
- blue_color (INTEGER, NOT NULL)
- brightness (INTEGER, NOT NULL)
- timestamp (TIMESTAMP, NOT NULL)
```

### Связи:
- **User ←→ TempAndHumidity**: Один пользователь может владеть множеством датчиков
- **User ←→ LedStrip**: Один пользователь может владеть множеством LED лент
- **TempAndHumidity ←→ TempAndHumidityData**: Один датчик может иметь множество записей телеметрии
- **LedStrip ←→ LedStripData**: Одна LED лента может иметь множество записей управления

## 🌐 API Endpoints

### 👥 Управление пользователями

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/v1/users` | Получить список всех пользователей |
| `GET` | `/api/v1/users/{id}` | Получить пользователя по ID |
| `POST` | `/api/v1/users` | Создать нового пользователя |
| `PUT` | `/api/v1/users/{id}` | Обновить пользователя |
| `DELETE` | `/api/v1/users/{id}` | Удалить пользователя (soft delete) |
| `GET` | `/api/v1/users/search?query={text}` | Поиск пользователей по имени/email |

### 🌡️ Управление датчиками температуры и влажности

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/v1/temp-humidity` | Получить список всех датчиков |
| `GET` | `/api/v1/temp-humidity/{id}` | Получить датчик по ID |
| `POST` | `/api/v1/temp-humidity` | Создать новый датчик |
| `PUT` | `/api/v1/temp-humidity/{id}` | Обновить датчик |
| `DELETE` | `/api/v1/temp-humidity/{id}` | Удалить датчик (soft delete) |
| `GET` | `/api/v1/temp-humidity/owner/{ownerId}` | Получить датчики пользователя |
| `GET` | `/api/v1/temp-humidity/active` | Получить все активные датчики |

### 💡 Управление LED лентами

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/v1/led-strip` | Получить список всех LED лент |
| `GET` | `/api/v1/led-strip/{id}` | Получить LED ленту по ID |
| `POST` | `/api/v1/led-strip` | Создать новую LED ленту |
| `PUT` | `/api/v1/led-strip/{id}` | Обновить LED ленту |
| `DELETE` | `/api/v1/led-strip/{id}` | Удалить LED ленту (soft delete) |
| `GET` | `/api/v1/led-strip/owner/{ownerId}` | Получить LED ленты пользователя |
| `GET` | `/api/v1/led-strip/active` | Получить все активные LED ленты |

### 📊 Управление данными температуры и влажности

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/v1/temp-humidity-data` | Получить все данные |
| `GET` | `/api/v1/temp-humidity-data/{id}` | Получить данные по ID |
| `POST` | `/api/v1/temp-humidity-data` | Добавить новые данные |
| `PUT` | `/api/v1/temp-humidity-data/{id}` | Обновить данные |
| `DELETE` | `/api/v1/temp-humidity-data/{id}` | Удалить данные |
| `GET` | `/api/v1/temp-humidity-data/device/{deviceId}` | Получить данные датчика |
| `GET` | `/api/v1/temp-humidity-data/device/{deviceId}/latest?limit={n}` | Последние данные датчика |
| `GET` | `/api/v1/temp-humidity-data/device/{deviceId}/period` | Данные за период |
| `GET` | `/api/v1/temp-humidity-data/device/{deviceId}/high-temperature` | Данные с высокой температурой |

### 🎨 Управление данными LED лент

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/v1/led-strip-data` | Получить все данные |
| `GET` | `/api/v1/led-strip-data/{id}` | Получить данные по ID |
| `POST` | `/api/v1/led-strip-data` | Добавить новые данные |
| `PUT` | `/api/v1/led-strip-data/{id}` | Обновить данные |
| `DELETE` | `/api/v1/led-strip-data/{id}` | Удалить данные |
| `GET` | `/api/v1/led-strip-data/device/{deviceId}` | Получить данные LED ленты |
| `GET` | `/api/v1/led-strip-data/device/{deviceId}/latest?limit={n}` | Последние состояния LED ленты |
| `GET` | `/api/v1/led-strip-data/device/{deviceId}/period` | Данные за период |
| `GET` | `/api/v1/led-strip-data/device/{deviceId}/latest-state` | Последнее состояние LED ленты |

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

### Создание датчика температуры и влажности:
```json
{
  "name": "Kitchen-TempSensor",
  "description": "Kitchen temperature and humidity sensor",
  "location": "Kitchen, 2nd floor",
  "ownerId": 1
}
```

### Создание LED ленты:
```json
{
  "name": "Living-Room-LEDs",
  "description": "RGB LED strip for ambient lighting",
  "location": "Living room, behind TV",
  "ownerId": 1
}
```

### Добавление данных температуры и влажности:
```json
{
  "deviceId": 1,
  "temperature": 23.5,
  "humidity": 45.2
}
```

### Добавление данных LED ленты:
```json
{
  "deviceId": 1,
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

- ✅ **Специализированные устройства**: Датчики температуры/влажности и LED ленты
- ✅ **Полный CRUD** для всех сущностей
- ✅ **Ролевая модель** пользователей
- ✅ **Связанные данные** User-Device-DeviceData
- ✅ **Кеширование** для оптимизации производительности
- ✅ **Поиск и фильтрация** по различным критериям
- ✅ **Soft Delete** для безопасного удаления
- ✅ **Validation** входящих данных
- ✅ **Timestamp tracking** для аудита
- ✅ **MapStruct маппинг** DTO ↔ Entity
- ✅ **BaseEntity** с общими audit полями

## 📈 Производительность

- **Spring Cache** для кеширования часто используемых данных
- **Connection Pooling** через HikariCP
- **Lazy Loading** для связанных сущностей
- **Intelligent Query Methods** для оптимизированных запросов
- **Unidirectional relationships** для избежания N+1 проблем

## 🛠️ Технические особенности

### BaseEntity для аудита:
```java
@MappedSuperclass
public abstract class BaseEntity {
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
    
    private LocalDateTime removedAt;
    
    @PrePersist, @PreUpdate, @PreRemove
    // JPA lifecycle methods
}
```

### MapStruct Configuration:
```java
@Mapper(componentModel = "spring")
public interface TempAndHumidityMapper {
    TempAndHumidityDto toDto(TempAndHumidity entity);
    TempAndHumidity toEntity(TempAndHumidityDto dto);
}
```

### Intelligent Repository Pattern:
```java
public interface TempAndHumidityRepository extends JpaRepository<TempAndHumidity, Long> {
    List<TempAndHumidity> findByOwnerIdAndRemovedAtIsNull(Long ownerId);
    List<TempAndHumidity> findByRemovedAtIsNull();
    Long countByOwnerIdAndRemovedAtIsNull(Long ownerId);
}
```

### Service Layer with Caching:
```java
@Service
@Transactional
public class TempAndHumidityServiceImpl {
    
    @Cacheable(value = "tempHumidityDevices", key = "#id")
    public TempAndHumidityDto getDeviceById(Long id);
    
    @CacheEvict(value = "tempHumidityDevices", allEntries = true)
    public void deleteDevice(Long id);
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
- [ ] MQTT integration для ESP32
- [ ] Time-series database для исторических данных

## 👨‍💻 Автор

**Timur** - [GitHub Profile](https://github.com/timurtm72)

## 📄 Лицензия

Этот проект разработан для учебных целей. 