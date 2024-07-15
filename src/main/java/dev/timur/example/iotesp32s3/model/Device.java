package dev.timur.example.iotesp32s3.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
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
@SQLDelete(sql = "UPDATE relay SET is_removed = true WHERE id = ?")
@Where(clause = "is_removed=false")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private Long id;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;
    @Column(name="removed_at")
    private LocalDateTime removedAt;
    @Column(name="modified_at")
    private LocalDateTime modifiedAt;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private List<BitDeviceData> inputValues;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private List<StripLedDeviceData> ledValues;

    @Column(name = "is_removed", nullable = false)
    private boolean isRemoved = Boolean.FALSE;

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
