package dev.timur.example.iotesp32s3.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import dev.timur.example.iotesp32s3.enums.DeviceStatus;

/**
 * Сущность IoT устройства
 * Универсальное устройство которое может содержать разные типы данных
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "device")
public class Device extends BaseEntity {
    
    /** Уникальный идентификатор устройства */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;
    
    /** Название устройства */
    @Column(name="name", nullable = false)
    private String name;
    
    /** Описание устройства */
    @Column(name="description")
    private String description;
    
    /** Местоположение устройства */
    @Embedded
    private Location location;

    /** Параметры WiFi устройства */
    @Embedded
    private WiFiParameters wifiParameters;

    /** Статус устройства */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeviceStatus status = DeviceStatus.OFFLINE;
    
    /** Данные LED ленты */
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LedStripData> ledStripData;
    
    /** Данные температуры и влажности */
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TempAndHumidityData> tempAndHumidityData;
    
    /** Владелец устройства - связь Many-to-One с User */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
} 