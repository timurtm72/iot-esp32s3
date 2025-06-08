package dev.timur.example.iotesp32s3.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@EqualsAndHashCode()
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "bit_device_data")
public class DeviceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private Long id;

    @Column(name="humidity",nullable = false)
    private Float humidity = 0.0F;

    @Column(name="temperature",nullable = false)
    private Float temperature = 0.0F;

    @Column(name="red_color",nullable = false)
    private Integer redColor = 0;

    @Column(name="green_color",nullable = false)
    private Integer greenColor = 0;

    @Column(name="blue_color",nullable = false)
    private Integer blueColor = 0;

    @Column(name="brightness",nullable = false)
    private Integer brightness = 0;

    @Column(name="timestamp",nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

}