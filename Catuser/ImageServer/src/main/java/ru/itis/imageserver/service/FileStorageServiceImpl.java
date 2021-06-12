package ru.itis.imageserver.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${cat.download.path}")
    private String imagesFolderPath;

    @SneakyThrows
    @Override
    public void writeFileToResponse(String filename, HttpServletResponse response) {
        response.setContentType("image");
        // получили инпут стрим файла на диске
        InputStream inputStream = new FileInputStream(new File(imagesFolderPath + filename));
        // скопировали файл в ответ
        org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
        // пробрасываем буфер
        response.flushBuffer();
    }
}
