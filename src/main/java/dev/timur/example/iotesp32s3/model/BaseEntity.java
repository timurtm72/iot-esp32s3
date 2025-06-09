package dev.timur.example.iotesp32s3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    
    @Column(name="created_at", nullable = false)
    protected LocalDateTime createdAt;
    
    @Column(name="modified_at")
    protected LocalDateTime modifiedAt;
    
    @Column(name="removed_at")
    protected LocalDateTime removedAt;
    
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