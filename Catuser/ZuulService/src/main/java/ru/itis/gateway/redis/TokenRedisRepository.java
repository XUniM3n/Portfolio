package ru.itis.gateway.redis;

public interface TokenRedisRepository {

    java.util.Map<String, String> findAllTokens();

    void save(String username, Long expiryDate);

    void delete(String username);

    Long findExpiryDateByUsername(String username);

}
