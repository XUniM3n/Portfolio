package ru.itis.services.cats.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.services.cats.dto.CatDto;
import ru.itis.services.cats.dto.UserDto;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("asyncService")
public class ImageServiceAsyncImpl implements ImageService {
    private final RestTemplate restTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ExecutorService service = Executors.newCachedThreadPool();

    @Value("${cat.download.path}")
    private String imagesFolderPath;

    @Value("${cat.search.url}")
    private String catSearchUrl;

    private Logger logger = LoggerFactory.getLogger(ImageServiceAsyncImpl.class);

    @Autowired
    public ImageServiceAsyncImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public void downloadImage(UserDto userDto) {
        Runnable getCatTask = () -> {
            System.out.println("Received " + userDto);
            CatDto catImage = Objects.requireNonNull(restTemplate
                    .getForEntity(catSearchUrl, CatDto[].class).getBody())[0];
            String catImageUrl = catImage.getUrl();
            String type = catImageUrl.substring(catImageUrl.lastIndexOf("."));
            logger.info("GET CAT IMAGE LINK " + catImageUrl);
            try {
                URL imageUrl = new URL(catImageUrl);
                Runnable downLoadImageTask = () -> {
                    try {
                        InputStream imageInputStream = imageUrl.openStream();
                        String newFileName = imagesFolderPath + UUID.randomUUID() + type;
                        Files.copy(imageInputStream, Paths.get(newFileName));
                        logger.info("DOWNLOADED " + newFileName);

                        userDto.setCatUrl(newFileName);
                        rabbitTemplate.convertAndSend("catuser-exchange", "catuser-front", userDto);
                        rabbitTemplate.convertAndSend("catuser-exchange", "catuser-db", userDto);
                        System.out.println("Sent " + userDto);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                };
                service.submit(downLoadImageTask);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        };

        service.submit(getCatTask);
    }
}
