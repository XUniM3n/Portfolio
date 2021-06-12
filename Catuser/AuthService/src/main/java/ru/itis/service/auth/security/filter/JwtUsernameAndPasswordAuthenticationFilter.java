//package ru.itis.service.auth.security.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.Getter;
//import lombok.Setter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import ru.itis.service.auth.redis.TokenRedisRepository;
//import ru.itis.service.auth.config.JwtConfig;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.security.Key;
//import java.security.KeyStore;
//import java.util.Collections;
//import java.util.Date;
//import java.util.stream.Collectors;
//
//public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private static final Logger logger = LoggerFactory.getLogger(JwtUsernameAndPasswordAuthenticationFilter.class);
//    private String jwtPassword;
//    private JwtConfig jwtConfig;
//    private TokenRedisRepository tokenRedisRepository;
//    // We use auth manager to validate the user credentials
//    private AuthenticationManager authManager;
//
//    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig,
//                                                      String jwtPassword, TokenRedisRepository tokenRedisRepository) {
//        this.authManager = authManager;
//        this.jwtConfig = jwtConfig;
//        this.jwtPassword = jwtPassword;
//        this.tokenRedisRepository = tokenRedisRepository;
//
//        // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
//        // In our case, we use "/auth". So, we need to override the defaults.
//        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
//
//        try {
//            // 1. Get credentials from request
//            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
//            System.out.println("Got creds " + creds.getUsername() + " " + creds.getPassword());
//
//            // 2. Create auth object (contains credentials) which will be used by auth manager
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                    creds.getUsername(), creds.getPassword(), Collections.emptyList());
//
//            // 3. Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.
//            return authManager.authenticate(authToken);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // Upon successful authentication, generate a token.
//    // The 'auth' passed to successfulAuthentication() is the current authenticated user.
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
//                                            Authentication auth) throws IOException, ServletException {
//        ClassPathResource resource = new ClassPathResource("keystore.jks");
//        KeyStore keystore;
//        Key key = null;
//        try {
//            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keystore.load(resource.getInputStream(), jwtPassword.toCharArray());
//            key = keystore.getKey("selfsigned", jwtPassword.toCharArray());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return;
//        }
//
//
//        Long now = System.currentTimeMillis();
//        Long expiryDate = now + jwtConfig.getExpiration() * 1000;
//        String username = auth.getName();
//        String token = Jwts.builder()
//                .setSubject(auth.getName())
//                // Convert to list of strings.
//                // This is important because it affects the way we get them back in the Gateway.
//                .claim("authorities", auth.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//                .setIssuedAt(new Date(now))
//                .setExpiration(new Date(expiryDate))  // in milliseconds
//                .signWith(SignatureAlgorithm.RS512, key)
//                .compact();
//
//        // Add token to header
//        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
//        tokenRedisRepository.save(username, expiryDate);
//    }
//
//    // A (temporary) class just to represent the user credentials
//    @Getter
//    @Setter
//    private static class UserCredentials {
//        private String username;
//        private String password;
//    }
//}
