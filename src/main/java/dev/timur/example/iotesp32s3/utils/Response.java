package dev.timur.example.iotesp32s3.utils;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class Response {
    private String message;
    private LocalDateTime timestamp;
}