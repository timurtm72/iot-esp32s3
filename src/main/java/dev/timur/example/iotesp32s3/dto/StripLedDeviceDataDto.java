package dev.timur.example.iotesp32s3.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StripLedDeviceDataDto {
    private Long id;
    @NotNull(message = "Значение индекса светодиодов не может быть null")
    private Integer index;
    @NotNull(message = "Значение цвета красного светодиода не может быть null")
    private Integer redColor;
    @NotNull(message = "Значение цвета зеленого светодиода не может быть null")
    private Integer greenColor;
    @NotNull(message = "Значение цвета синего светодиода не может быть null")
    private Integer blueColor;
    private LocalDateTime timestamp;
}
