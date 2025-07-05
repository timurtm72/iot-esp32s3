package dev.timur.example.iotesp32s3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import jakarta.validation.constraints.Min;

/**
 * Встроенный класс для описания адреса устройства.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Location {

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Min(0)
    @Column(name = "house")
    private Integer house;

    @Min(0)
    @Column(name = "apartment")
    private Integer apartment;

    @Column(name = "room")
    private String room;
} 