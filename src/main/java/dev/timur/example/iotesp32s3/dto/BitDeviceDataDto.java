package dev.timur.example.iotesp32s3.dto;

import dev.timur.example.iotesp32s3.model.Device;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BitDeviceDataDto {
    private Long id;
    @NotNull(message = "Значение входа устройства не может быть null")
    private Boolean value;
    private LocalDateTime timestamp;
}
