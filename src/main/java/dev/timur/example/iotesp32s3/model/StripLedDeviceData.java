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
@Table(name = "strip_led_data")
@SQLDelete(sql = "UPDATE relay SET is_removed = true WHERE id = ?")
@Where(clause = "is_removed=false")
public class StripLedDeviceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private Long id;
    @Column(name="index",nullable = false)
    private Integer index = 0;
    @Column(name="red_color",nullable = false)
    private Integer redColor = 0;
    @Column(name="green_color",nullable = false)
    private Integer greenColor = 0;
    @Column(name="blue_color",nullable = false)
    private Integer blueColor = 0;
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

