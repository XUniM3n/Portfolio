package ru.itis.front.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileStorageService {
    void writeFileToResponse(String fileName, HttpServletResponse response);
}
