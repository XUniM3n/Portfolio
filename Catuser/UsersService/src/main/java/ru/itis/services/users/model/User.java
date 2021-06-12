package ru.itis.services.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.services.users.dto.UserDto;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(length = 30)
    private String username;
    @Column(length = 64)
    private String password;
    @Column(length = 128)
    private String catUrl;
    @Column(length = 20)
    private String role;
    @Transient
    private String sessionId;

    public User(UserDto dto){
        username = dto.getUsername();
        password = dto.getPassword();
        catUrl = dto.getCatUrl();
    }
}
