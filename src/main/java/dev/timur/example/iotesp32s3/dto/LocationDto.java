package dev.timur.example.iotesp32s3.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO локации устройства.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotBlank
    private String country;
    @NotBlank
    private String city;
    private String street;
    @Min(0)
    private Integer house;
    @Min(0)
    private Integer apartment;
    private String room;
} 