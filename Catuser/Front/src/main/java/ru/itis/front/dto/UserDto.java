package ru.itis.front.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    private String id;
    private String sessionId;
    private String username;
    private String password;
    private String catUrl;
    private String role;
}
