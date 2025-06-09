package dev.timur.example.iotesp32s3.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Сущность устройства для измерения температуры и влажности
 * Представляет собой IoT устройство с датчиками температуры и влажности
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "temp_and_humidity")
public class TempAndHumidity extends BaseEntity {
    
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
    @Column(name="location", length = 500)
    private String location;
    
    /** Данные с датчиков температуры и влажности */
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TempAndHumidityData> dataValues;
    
    /** Владелец устройства - связь Many-to-One с User */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
} 