package ru.itis.services.cats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.services.cats.dto.CatDto;

/**
 * 07.04.2018
 * CatService
 *
 * @author Sidikov Marsel (First Software Engineering Platform)
 * @version v1.0
 */
@Service
public class CatServiceImpl implements CatService {

    private final RestTemplate restTemplate;

    @Value("${cat.search.url}")
    private String catsGetRequestUrl;

    @Autowired
    public CatServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public CatDto getCat() {
        return restTemplate.getForEntity(catsGetRequestUrl, CatDto[].class).getBody()[0];
    }
}
