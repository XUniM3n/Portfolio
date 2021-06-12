package ru.itis.services.cats.service;

import ru.itis.services.cats.dto.UserDto;

public interface ImageService {
    void downloadImage(UserDto userDto);
}
