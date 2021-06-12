package ru.itis.services.cats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {
    private String sessionId;
    private String username;
    private String password;
    private String catUrl;
}
