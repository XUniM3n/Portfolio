package ru.itis.services.cats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.services.cats.dto.CatDto;
import ru.itis.services.cats.service.CatService;

/**
 * 07.04.2018
 * VkUsersController
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
@RestController
public class CatsUsersController {

    private final CatService service;

    @Autowired
    public CatsUsersController(CatService service) {
        this.service = service;
    }

    @GetMapping("/cats/search")
    public CatDto getCat() {
        return service.getCat();
    }
}
