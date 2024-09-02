package dev.timur.example.iotesp32s3.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DeviceDataDto {
    private Long id;

    private Boolean mode = false;

    @NotNull(message = "Значение входов устройства не может быть null")
    private Short inputValue = 0;

    @NotNull(message = "Значение выходов устройства не может быть null")
    private Short outputValue = 0;

    @NotNull(message = "Значение влажности не может быть null")
    private Float humidity = 0.0F;

    @NotNull(message = "Значение влажности не может быть null")
    private Float temperature = 0.0F;

//    private LocalDateTime timestamp;
}
