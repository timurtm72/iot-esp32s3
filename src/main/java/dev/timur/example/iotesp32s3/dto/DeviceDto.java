package dev.timur.example.iotesp32s3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DeviceDto {
    private Long id;
    @NotBlank(message = "Название устройства не может быть пустым")
    private String name;
    private String description;
    @NotNull(message = "Список значений входа не может быть пустым")
    private List<BitDeviceDataDto> inputValues;
    @NotNull(message = "Список значений светодиодов не может быть пустым")
    private List<StripLedDeviceDataDto> ledValues;
}
