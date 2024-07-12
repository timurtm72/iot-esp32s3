package dev.timur.example.iotesp32s3.dto;

import dev.timur.example.iotesp32s3.model.Device;
import lombok.*;
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BitDeviceDataDto {
    private Long id;
    private Boolean value;
}
