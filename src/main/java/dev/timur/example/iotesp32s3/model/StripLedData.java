package dev.timur.example.iotesp32s3.model;

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
public class StripLedData {
    Integer index;
    Integer redColor;
    Integer greenColor;
    Integer blueColor;
    private LocalDateTime timestamp;
}

