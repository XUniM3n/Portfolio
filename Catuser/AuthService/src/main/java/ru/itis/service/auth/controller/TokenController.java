package ru.itis.service.auth.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.service.auth.config.JwtConfig;
import ru.itis.service.auth.dto.MessageDto;
import ru.itis.service.auth.dto.TokenRevokeDto;
import ru.itis.service.auth.dto.UserCredentials;
import ru.itis.service.auth.model.User;
import ru.itis.service.auth.redis.TokenRedisRepository;
import ru.itis.service.auth.repository.UserRepository;

import java.security.Key;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TokenController {
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRedisRepository tokenRedisRepository;
    @Value("${jwt.cert.password}")
    private String jwtPassword;

    @Autowired
    public TokenController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtConfig jwtConfig,
                           TokenRedisRepository tokenRedisRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.tokenRedisRepository = tokenRedisRepository;
    }

    @PostMapping("/auth")
    public MessageDto getToken(@RequestBody UserCredentials creds) {
        Optional<User> optionalUser = userRepository.findByUsername(creds.getUsername());

        if (!optionalUser.isPresent())
            return invalidCredsMessage();

        User user = optionalUser.get();
        if (!passwordEncoder.matches(creds.getPassword(), user.getPassword()))
            return invalidCredsMessage();

        String username = user.getUsername();
        System.out.printf("Received user [%s; %s]\n", username, user.getPassword());

        ClassPathResource resource = new ClassPathResource("keystore.jks");
        KeyStore keystore;
        Key key = null;
        try {
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(resource.getInputStream(), jwtPassword.toCharArray());
            key = keystore.getKey("selfsigned", jwtPassword.toCharArray());
        } catch (Exception ex) {
            ex.printStackTrace();
            return MessageDto.builder().title("ERROR").text("Unable to get key").build();
        }

        String role = "ROLE_USER";
        Long now = System.currentTimeMillis();
        Long expiryDate = now + jwtConfig.getExpiration() * 1000;
        String token = Jwts.builder()
                .setSubject(username)
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", Arrays.asList(role))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiryDate))  // in milliseconds
                .signWith(SignatureAlgorithm.RS512, key)
                .compact();

        tokenRedisRepository.save(username, expiryDate);

        return MessageDto.builder().title("SUCCESS").text(token).build();
    }

    private MessageDto invalidCredsMessage() {
        return MessageDto.builder().title("ERROR").text("Invalid username or password").build();
    }

    @PostMapping(path = "/revoke", produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDto revokeToken(@RequestBody TokenRevokeDto[] tokensRevoke) {
        Arrays.stream(tokensRevoke).forEach(t -> {
            System.out.printf("Revoke token for %s\n", t.getUsername());
            tokenRedisRepository.delete(t.getUsername());
        });
        return MessageDto.builder().title("SUCCESS").build();
    }
}
