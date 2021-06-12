package ru.itis.front.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;
import ru.itis.front.config.GatewayConfig;
import ru.itis.front.dto.MessageDto;
import ru.itis.front.dto.TokenRevokeDto;
import ru.itis.front.dto.UserDto;
import ru.itis.front.form.UserAuthForm;
import ru.itis.front.form.UsernameForm;
import ru.itis.front.service.FileStorageService;
import ru.itis.front.service.RegistrationService;
import ru.itis.front.validation.UserRegistrationFormValidator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

@Controller
public class MainController {
    private final UserRegistrationFormValidator userRegistrationFormValidator;
    private final RegistrationService registrationService;
    private final FileStorageService fileStorageService;
    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson;
    private String usersUrl;
    private String revokeTokenUrl;

    @Autowired
    public MainController(UserRegistrationFormValidator userRegistrationFormValidator,
                          RegistrationService registrationService, FileStorageService fileStorageService,
                          Gson gson, GatewayConfig gatewayConfig) {
        this.userRegistrationFormValidator = userRegistrationFormValidator;
        this.registrationService = registrationService;
        this.fileStorageService = fileStorageService;
        this.gson = gson;
        usersUrl = gatewayConfig.getGateway() + gatewayConfig.getGatewayPrefix() +
                gatewayConfig.getUserServiceRoute() + "/users";
        revokeTokenUrl = gatewayConfig.getGateway() + gatewayConfig.getGatewayPrefix() +
                gatewayConfig.getAuthServiceRoute() + "/revoke";
    }

    @GetMapping(value = "/")
    public String signUp() {
        return "signup";
    }

    @PostMapping(value = "/")
    public String signUp(@Valid @ModelAttribute("regForm") UserAuthForm form, BindingResult errors,
                         RedirectAttributes attributes, ServerHttpRequest request) {
        if (!errors.hasErrors()) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            registrationService.register(form, session.getId());
        }
        return "redirect:/";
    }

    @GetMapping(value = "/users")
    public String signUp(Model model, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Cookie jwtCookie = WebUtils.getCookie(request, "JWT");
        if (jwtCookie == null) {
            model.addAttribute("errorTitle", "401 ERROR");
            model.addAttribute("timestamp", new Date().toString());
            return "error";
        }
        headers.add("Authorization", "Bearer " + jwtCookie.getValue());
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            UserDto[] users = restTemplate.exchange(usersUrl, HttpMethod.GET,
                    requestEntity, UserDto[].class).getBody();
            model.addAttribute("users", users);
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            if (statusCode == 401) {
                model.addAttribute("errorTitle", "401 ERROR");
                model.addAttribute("timestamp", new Date().toString());
                return "error";
            }
        }

        return "users";
    }

    @PostMapping("/users/revoke")
    public String revokeToken(UsernameForm form, HttpServletRequest request, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Cookie jwtCookie = WebUtils.getCookie(request, "JWT");
        if (jwtCookie == null) {
            model.addAttribute("errorTitle", "401 Unauthorized error");
            model.addAttribute("timestamp", new Date().toString());
            return "error";
        }
        headers.add("Authorization", "Bearer " + jwtCookie.getValue());

        TokenRevokeDto[] tokensRevoke = new TokenRevokeDto[1];
        tokensRevoke[0] = new TokenRevokeDto(form.getUsername());

        HttpEntity<String> httpEntity = new HttpEntity<String>(gson.toJson(tokensRevoke), headers);
        try {
            MessageDto message = restTemplate.postForEntity(revokeTokenUrl, httpEntity, MessageDto.class).getBody();
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            if (statusCode == 401) {
                model.addAttribute("errorTitle", "401 Unauthorized error");
                model.addAttribute("timestamp", new Date().toString());
                return "error";
            }
        }
        return "redirect:/users";
    }

    @InitBinder("regForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(userRegistrationFormValidator);
    }

    @GetMapping("/image/{file-name:.+}")
    public void getFile(@PathVariable("file-name") String fileName,
                        HttpServletResponse response) {
        fileStorageService.writeFileToResponse(fileName, response);
    }
}
