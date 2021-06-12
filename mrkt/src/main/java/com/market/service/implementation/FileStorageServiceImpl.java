package com.market.service.implementation;

import com.market.model.FileInfo;
import com.market.repository.FileInfoRepository;
import com.market.service.FileStorageService;
import com.market.util.FileStorageUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final FileInfoRepository fileInfoRepository;
    private final FileStorageUtil fileStorageUtil;

    @Autowired
    public FileStorageServiceImpl(FileInfoRepository fileInfoRepository, FileStorageUtil fileStorageUtil) {
        this.fileInfoRepository = fileInfoRepository;
        this.fileStorageUtil = fileStorageUtil;
    }

    @Override
    public String saveFile(MultipartFile file) {
        // конвертируем из Multipart в понятный для нас объект БД
        FileInfo fileInfo = fileStorageUtil.convertFromMultipart(file);
        // сохраняем информацию о файле
        fileInfoRepository.save(fileInfo);
        // переносим файл на наш диск
        fileStorageUtil.copyToStorage(file, fileInfo.getStorageFileName());
        // возвращаем имя файла - новое
        return fileInfo.getStorageFileName();
    }

    @SneakyThrows
    @Override
    public void writeFileToResponse(String fileName, HttpServletResponse response) {

        FileInfo file = fileInfoRepository.findOneByStorageFileName(fileName);
        response.setContentType(file.getType());
        // получили инпут стрим файла на диске
        InputStream inputStream = new FileInputStream(new java.io.File(file.getUrl()));
        // скопировали файл в ответ
        org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
        // пробрасываем буфер
        response.flushBuffer();
    }
}
