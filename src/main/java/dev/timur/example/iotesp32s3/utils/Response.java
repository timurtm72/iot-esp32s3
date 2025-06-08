package dev.timur.example.iotesp32s3.utils;

import dev.timur.example.iotesp32s3.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Response<T> {
    private T data;
    private Status status;
    private LocalDateTime timestamp;

    public Response(T data, Status status) {
        this.data = data;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}