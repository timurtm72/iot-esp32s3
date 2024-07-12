package dev.timur.example.iotesp32s3.dto;

import dev.timur.example.iotesp32s3.model.Device;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StripLedDataDto {
    private Long id;
    private Integer index;
    private Integer redColor;
    private Integer greenColor;
    private Integer blueColor;
}
