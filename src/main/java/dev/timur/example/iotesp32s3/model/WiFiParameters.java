package dev.timur.example.iotesp32s3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Встроенный класс для параметров WiFi устройства.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class WiFiParameters {

    @Column(name = "wifi_ssid")
    private String ssid;

    @Column(name = "wifi_password")
    private String password;

    @Column(name = "wifi_ip_address")
    private String ipAddress;

    @Column(name = "wifi_mac_address")
    private String macAddress;
} 