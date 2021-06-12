package ru.itis.front.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.front.dto.UserDto;
import ru.itis.front.form.UserAuthForm;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RegistrationServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void register(UserAuthForm form, String sessionId) {
        UserDto user = UserDto.builder().sessionId(sessionId).username(form.getUsername()).password(form.getPassword()).build();
        System.out.println("User entered form" + user.toString());
        rabbitTemplate.convertAndSend("user-exchange", "user", user);
    }
}
