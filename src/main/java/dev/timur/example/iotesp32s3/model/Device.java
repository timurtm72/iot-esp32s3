package dev.timur.example.iotesp32s3.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode()
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private Long id;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="location", length = 500)
    private String location;
    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;
    @Column(name="removed_at")
    private LocalDateTime removedAt;
    @Column(name="modified_at")
    private LocalDateTime modifiedAt;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private List<DeviceData> dataValues;
    
    /** Владелец устройства - связь Many-to-One с User */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @PrePersist
    public void toCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void toModified() {
        this.modifiedAt = LocalDateTime.now();
    }

    @PreRemove
    public void toRemove() {
        this.removedAt = LocalDateTime.now();
    }
}
