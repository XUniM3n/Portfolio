package ru.itis.imageserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.imageserver.service.FileStorageService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Random;

@Controller
public class ImageController {

    private final FileStorageService fileStorageService;
    @Value("${cat.download.path}")
    private String catDownloadPath;
    private Random random = new Random();

    @Autowired
    public ImageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/cat.jpg")
    public void getRandomImage(HttpServletResponse response) {
        File[] fileList = new File(catDownloadPath).listFiles();
        String randomFile = fileList[random.nextInt(fileList.length)].getName();
        fileStorageService.writeFileToResponse(randomFile, response);
    }
}
