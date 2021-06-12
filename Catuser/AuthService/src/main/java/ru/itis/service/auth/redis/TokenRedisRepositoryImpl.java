package ru.itis.service.auth.redis;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenRedisRepositoryImpl implements TokenRedisRepository {
    private static final String KEY = "JWT";

    private final RedisCommands<String, String> commands;

    @Autowired
    public TokenRedisRepositoryImpl(StatefulRedisConnection<String, String> connection) {
        commands = connection.sync();
    }

    @Override
    public void save(final String username, final Long expiryDate) {
        commands.hset(KEY, username, expiryDate.toString());
    }

    @Override
    public void delete(final String username) {
        commands.hdel(KEY, username);
    }

    @Override
    public Long findExpiryDateByUsername(final String username) {
        return Long.parseLong(commands.hget(KEY, username));
    }

    @Override
    public Map<String, String> findAllTokens() {
        return commands.hgetall(KEY);
    }

}
