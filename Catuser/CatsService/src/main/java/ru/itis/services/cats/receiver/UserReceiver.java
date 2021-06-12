package ru.itis.services.cats.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.services.cats.dto.UserDto;
import ru.itis.services.cats.service.ImageService;

@Component
public class UserReceiver {
    private final ImageService imageService;

    @Autowired
    public UserReceiver(ImageService imageService) {
        this.imageService = imageService;
    }

    @RabbitListener(queues = "user")
    public void process(UserDto userDto) {
        imageService.downloadImage(userDto);
    }
}
