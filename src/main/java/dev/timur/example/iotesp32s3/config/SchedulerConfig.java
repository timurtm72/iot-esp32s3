package dev.timur.example.iotesp32s3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Конфигурация включает поддержку планировщика Spring (@Scheduled).
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
} 