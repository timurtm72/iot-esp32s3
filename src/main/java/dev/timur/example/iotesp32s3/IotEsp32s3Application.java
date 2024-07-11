package dev.timur.example.iotesp32s3;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IotEsp32s3Application {

    public static void main(String[] args) {

        SpringApplication.run(IotEsp32s3Application.class, args);
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
