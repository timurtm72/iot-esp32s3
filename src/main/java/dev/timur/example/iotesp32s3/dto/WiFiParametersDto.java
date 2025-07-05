package dev.timur.example.iotesp32s3.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO параметров WiFi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WiFiParametersDto {
    @NotBlank
    private String ssid;
    @NotBlank
    private String password;
    private String ipAddress;
    private String macAddress;
} 