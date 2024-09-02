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
@SQLDelete(sql = "UPDATE relay SET is_removed = true WHERE id = ?")
@Where(clause = "is_removed=false")
public class DeviceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private Long id;

    @Column(name="mode",nullable = false)
    private Boolean mode = false;

    @Column(name="input_value",nullable = false)
    private Short inputValue = 0;

    @Column(name="output_value",nullable = false)
    private Short outputValue = 0;

    @Column(name="humidity",nullable = false)
    private Float humidity = 0.0F;

    @Column(name="temperature",nullable = false)
    private Float temperature = 0.0F;

    @Column(name="timestamp",nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "is_removed", nullable = false)
    private boolean isRemoved = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private Device device;
    @PrePersist
    public void toCreate() {
        this.timestamp = LocalDateTime.now();
    }

    @PreUpdate
    public void toModified() {
        this.timestamp = LocalDateTime.now();
    }

    @PreRemove
    public void toRemove() {
        this.timestamp = LocalDateTime.now();
    }

}