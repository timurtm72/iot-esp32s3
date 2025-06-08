package dev.timur.example.iotesp32s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Главный класс Spring Boot приложения для системы управления IoT устройствами ESP32S3.
 * 
 * Система предоставляет:
 * - Управление пользователями с ролевой моделью и безопасностью
 * - Регистрацию и мониторинг IoT устройств ESP32S3
 * - Сбор и анализ данных телеметрии (температура, влажность, RGB, яркость)
 * - RESTful API для взаимодействия с фронтендом и устройствами
 * - Поддержку временных рядов данных с оптимизированными запросами
 * - Кеширование для повышения производительности
 * - Современную архитектуру с MapStruct маппингом и Specifications
 * 
 * Конфигурация:
 * - Spring Boot автоконфигурация
 * - Включено кеширование для оптимизации производительности
 * - PostgreSQL как основная база данных
 * - JPA/Hibernate для работы с данными
 * - MapStruct для безопасного маппинга DTO
 * 
 * @author Timur
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
@EnableCaching
public class IotEsp32S3Application {

    /**
     * Точка входа в приложение Spring Boot.
     * Запускает встроенный веб-сервер и инициализирует контекст приложения.
     * 
     * @param args аргументы командной строки для конфигурации приложения
     */
    public static void main(String[] args) {
        SpringApplication.run(IotEsp32S3Application.class, args);
    }
}
