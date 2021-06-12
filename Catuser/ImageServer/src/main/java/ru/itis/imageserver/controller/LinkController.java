package ru.itis.imageserver.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.imageserver.dto.CatDto;

@RestController
public class LinkController {
    private final Gson gson;
    @Value("${cat.download.url}")
    private String catDownloadUrl;

    public LinkController(Gson gson) {
        this.gson = gson;
    }

    @GetMapping(path = "/v1/images/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public CatDto[] getLink() {
        CatDto catDto = CatDto.builder().url(catDownloadUrl).build();
        CatDto[] cats = new CatDto[1];
        cats[0]=catDto;
        return cats;
    }
}
