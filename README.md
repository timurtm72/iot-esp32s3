# IoT ESP32 Spring Boot API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue)
![MapStruct](https://img.shields.io/badge/MapStruct-1.5.5-yellow)

Современное REST API для управления IoT устройствами ESP32 с LED лентами и датчиками температуры/влажности.

## 📋 Оглавление

- [Описание проекта](#описание-проекта)
- [Технологический стек](#технологический-стек)
- [Архитектура](#архитектура)
- [Установка и запуск](#установка-и-запуск)
- [Структура проекта](#структура-проекта)
- [API Endpoints](#api-endpoints)
- [Модели данных](#модели-данных)
- [Конфигурация](#конфигурация)
- [Разработка](#разработка)

## 🚀 Описание проекта

Это RESTful API для управления IoT устройствами ESP32, которые контролируют LED ленты и собирают данные с датчиков температуры и влажности. Проект предоставляет полный CRUD функционал для управления устройствами, пользователями и сбором телеметрии.

### Основные возможности:

- 👤 Управление пользователями с системой ролей
- 🔧 Регистрация и управление IoT устройствами
- 💡 Контроль LED лент (цвет RGB, яркость)
- 🌡️ Сбор и хранение данных датчиков температуры/влажности
- 🔒 Безопасная аутентификация и авторизация
- 📊 Кеширование для оптимизации производительности
- 🗄️ Аудит данных (soft delete, timestamp)

## 🛠 Технологический стек

### Backend
- **Java 17** - Основной язык программирования
- **Spring Boot 3.3.1** - Основной фреймворк
- **Spring Data JPA** - ORM и работа с базой данных
- **Spring Cache** - Кеширование
- **Spring Validation** - Валидация данных
- **Spring Web** - REST API

### Database
- **PostgreSQL** - Основная база данных
- **Hibernate** - JPA провайдер
- **HikariCP** - Connection pooling

### Tools & Libraries
- **MapStruct** - Маппинг между DTO и Entity
- **Lombok** - Генерация boilerplate кода
- **Maven** - Управление зависимостями и сборка

## 🏗 Архитектура

Проект следует принципам чистой архитектуры с разделением на слои:

```
src/main/java/
├── model/          # Сущности JPA
├── dto/            # Data Transfer Objects
├── repository/     # Слой доступа к данным
├── service/        # Бизнес-логика (интерфейсы)
├── serviceimpl/    # Реализация сервисов
├── mapper/         # MapStruct мапперы
└── controller/     # REST контроллеры (планируется)
```

### Ключевые принципы:
- **Dependency Injection** - Слабая связанность компонентов
- **Interface Segregation** - Разделение интерфейсов и реализации
- **Single Responsibility** - Каждый класс отвечает за одну задачу
- **MapStruct Integration** - Автоматическая генерация мапперов

## 📦 Установка и запуск

### Предварительные требования

- Java 17+
- PostgreSQL 12+
- Maven 3.8+

### Клонирование репозитория

```bash
git clone https://github.com/yourusername/iot-esp32s3.git
cd iot-esp32s3
```

### Настройка базы данных

1. Создайте базу данных PostgreSQL:
```sql
CREATE DATABASE iot_esp32;
CREATE USER iot_user WITH PASSWORD 'iot_password';
GRANT ALL PRIVILEGES ON DATABASE iot_esp32 TO iot_user;
```

2. Обновите `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/iot_esp32
spring.datasource.username=iot_user
spring.datasource.password=iot_password
```

### Запуск приложения

```bash
# Сборка проекта
./mvnw clean compile

# Запуск приложения
./mvnw spring-boot:run
```

Приложение будет доступно по адресу: `http://localhost:8081`

## 📁 Структура проекта

### Модели данных

#### BaseEntity
Базовая сущность с аудитом:
- `createdAt` - Время создания
- `modifiedAt` - Время последнего изменения  
- `removedAt` - Время удаления (soft delete)

#### User
Пользователи системы:
- Аутентификация (username, email, password)
- Роли (USER_ROLE, OPERATOR_ROLE, ADMIN_ROLE)
- Персональная информация

#### Device
IoT устройства:
- Основная информация (название, описание, местоположение)
- Связь с владельцем (User)
- Связь с данными датчиков

#### LedStripData
Данные LED ленты:
- RGB цвета (red, green, blue)
- Яркость (brightness)
- Временная метка

#### TempAndHumidityData
Данные датчиков:
- Температура (temperature)
- Влажность (humidity)  
- Временная метка

### Сервисы

Каждая сущность имеет соответствующий сервис с полным CRUD функционалом:

```java
// Пример методов сервиса
public interface DeviceService {
    DeviceReadDto create(DeviceDto deviceDto);
    DeviceReadDto findById(Long id);
    List<DeviceReadDto> findAll();
    DeviceReadDto update(Long id, DeviceDto deviceDto);
    void deleteById(Long id);
    // + специфичные методы
}
```

### MapStruct Мапперы

Автоматическая генерация мапперов между DTO и Entity:
- Игнорирование служебных полей при создании
- Правильная обработка связей
- Поддержка Spring DI

## 🔧 Конфигурация

### application.properties

```properties
# Настройки сервера
server.port=8081

# База данных
spring.datasource.url=jdbc:postgresql://localhost:5432/iot_esp32
spring.datasource.username=iot_user
spring.datasource.password=iot_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Кеширование
spring.cache.type=simple

# Кодировка
spring.datasource.sql-script-encoding=UTF-8
```

## 👨‍💻 Разработка

### Добавление новой сущности

1. Создайте Entity в пакете `model`
2. Создайте DTO в пакете `dto`
3. Создайте Repository в пакете `repository`
4. Создайте Service интерфейс в пакете `service`
5. Создайте Service реализацию в пакете `serviceimpl`
6. Создайте MapStruct маппер в пакете `mapper`

### Соглашения по коду

- Используйте русские комментарии для документации
- Применяйте аннотации Lombok для сокращения boilerplate кода
- Все сервисы должны быть транзакционными (@Transactional)
- Используйте кеширование для часто запрашиваемых данных
- Логируйте важные операции с помощью @Slf4j

### Тестирование

```bash
# Запуск тестов
./mvnw test

# Сборка с тестами
./mvnw clean package
```

## 📈 Планы развития

- [ ] REST контроллеры для всех сущностей
- [ ] Swagger/OpenAPI документация
- [ ] JWT аутентификация
- [ ] WebSocket для real-time данных
- [ ] Docker контейнеризация
- [ ] CI/CD pipeline
- [ ] Метрики и мониторинг
- [ ] Интеграционные тесты

## 🤝 Вклад в проект

1. Форкните репозиторий
2. Создайте feature branch (`git checkout -b feature/amazing-feature`)
3. Закоммитьте изменения (`git commit -m 'Add amazing feature'`)
4. Запушьте в branch (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

## 📄 Лицензия

Этот проект распространяется под лицензией MIT. См. файл `LICENSE` для подробностей.

## 📞 Контакты

- **Автор**: Timur
- **Email**: your-email@example.com
- **GitHub**: [@yourusername](https://github.com/yourusername)

---

⭐ Если проект был полезен, поставьте звездочку! 