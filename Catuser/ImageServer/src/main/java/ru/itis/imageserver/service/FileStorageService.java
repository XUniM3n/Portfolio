package ru.itis.imageserver.service;

import javax.servlet.http.HttpServletResponse;

public interface FileStorageService {
    void writeFileToResponse(String fileName, HttpServletResponse response);
}
