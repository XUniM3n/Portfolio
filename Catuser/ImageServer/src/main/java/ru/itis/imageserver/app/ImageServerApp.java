package ru.itis.imageserver.app;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.itis.imageserver")
public class ImageServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ImageServerApp.class, args);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}