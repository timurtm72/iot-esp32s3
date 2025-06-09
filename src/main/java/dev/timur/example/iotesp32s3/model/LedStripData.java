package dev.timur.example.iotesp32s3.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Сущность для хранения данных управления LED лентой
 */
@EqualsAndHashCode()
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "led_strip_data")
public class LedStripData {
    
    /** Уникальный идентификатор записи данных */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    /** Значение красного цвета (0-255) */
    @Column(name="red_color", nullable = false)
    private Integer redColor = 0;

    /** Значение зеленого цвета (0-255) */
    @Column(name="green_color", nullable = false)
    private Integer greenColor = 0;

    /** Значение синего цвета (0-255) */
    @Column(name="blue_color", nullable = false)
    private Integer blueColor = 0;

    /** Яркость LED ленты (0-255) */
    @Column(name="brightness", nullable = false)
    private Integer brightness = 0;

    /** Время установки значений */
    @Column(name="timestamp", nullable = false)
    private LocalDateTime timestamp;

    /** Связь с устройством */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
} 