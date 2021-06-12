package ru.itis.front.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.itis.front.dto.UserDto;
import ru.itis.front.dto.WebSocketMessageDto;

@Component
public class CatUserReceiver {

    private final SimpMessagingTemplate simpMessagingTemplate;
    @Value("${front.server.address}")
    private String frontServerAddress;
    @Value("${server.port}")
    private String frontServerPort;

    @Autowired
    public CatUserReceiver(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @RabbitListener(queues = "catuser-front")
    public void process(UserDto dto) {
        System.out.println("Received catuser: " + dto);
        String filename = dto.getCatUrl().substring(dto.getCatUrl().lastIndexOf("\\") + 1);
        String link = "\"http://" + frontServerAddress + ":" + frontServerPort + "/image/" + filename + "\"";
        simpMessagingTemplate.convertAndSendToUser(dto.getSessionId(), "/queue/signup",
                new WebSocketMessageDto("Successfully registered. Your cat <a href=" + link + ">" + link + "</a>"));

    }
}
