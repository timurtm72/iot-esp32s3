package dev.timur.example.iotesp32s3.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Сущность для хранения данных с датчиков температуры и влажности
 */
@EqualsAndHashCode()
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "temp_and_humidity_data")
public class TempAndHumidityData {
    
    /** Уникальный идентификатор записи данных */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    /** Значение влажности в процентах */
    @Column(name="humidity", nullable = false)
    private Float humidity = 0.0F;

    /** Значение температуры в градусах Цельсия */
    @Column(name="temperature", nullable = false)
    private Float temperature = 0.0F;

    /** Время снятия показаний с датчиков */
    @Column(name="timestamp", nullable = false)
    private LocalDateTime timestamp;

    /** Связь с устройством */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
} 