package com.market.service.implementation;

import com.market.model.User;
import com.market.repository.UserRepository;
import com.market.security.role.Role;
import com.market.service.AdminService;
import com.market.service.EmailService;
import com.market.util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    //    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public List<User> getAllUsers() {
        return usersRepository.findAllByRole(Role.USER);
    }

    //@Transactional
    @Override
    public void createTempPassword(Long userId) {
        Optional<User> existedUser = usersRepository.findById(userId);
        // никому не говорите
        User admin = usersRepository.findById(1L).get();

        if (!existedUser.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = existedUser.get();

        String tempPassword = passwordGenerator.generate();

        usersRepository.save(user);

        //        executorService.submit(() -> {
        //            emailService.sendMail("<h1>" + tempPassword + "</h1>",
        //                    "Временный пароль для пользователя " + user.getLogin(),
        //                    admin.getEmail());
        //        });
    }

    @Override
    public void clearTempPassword(Long userId) {
        Optional<User> existedUser = usersRepository.findById(userId);

        if (!existedUser.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = existedUser.get();
        usersRepository.save(user);
    }
}
