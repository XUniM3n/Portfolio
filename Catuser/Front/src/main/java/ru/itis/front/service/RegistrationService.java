package ru.itis.front.service;

import ru.itis.front.form.UserAuthForm;

public interface RegistrationService {
    void register(UserAuthForm form, String sessionId);
}
