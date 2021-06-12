package ru.itis.front.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;
import ru.itis.front.config.GatewayConfig;
import ru.itis.front.dto.MessageDto;
import ru.itis.front.form.UserAuthForm;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class LoginController {

    private String authUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private Gson gson;

    @Autowired
    public LoginController(Gson gson, GatewayConfig gwConfig) {
        this.gson = gson;
        authUrl = gwConfig.getGateway() + gwConfig.getGatewayPrefix() + gwConfig.getAuthServiceRoute() + "/auth";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request) {
        // If user already has token/
        if (WebUtils.getCookie(request, "JWT") != null)
            return "redirect:/token";

        return "login";
    }

    @GetMapping("/token")
    public String tokenPage(Model model, HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "JWT");

        // If user has no token.
        if (cookie == null)
            return "redirect:/login";

        model.addAttribute("token", "Your token " + cookie.getValue());

        return "token";
    }

    @PostMapping("/login")
    public String login(Model model, UserAuthForm form, HttpServletResponse response) {
        String requestJson = gson.toJson(form);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> userEntity = new HttpEntity<>(requestJson, headers);

        MessageDto messageDto = MessageDto.builder().title("Unsuccessfull request").build();

        try {
            // MessageDto.getText() = token
            messageDto = restTemplate.exchange(authUrl, HttpMethod.POST, userEntity, MessageDto.class).getBody();
            if (messageDto == null || !messageDto.getTitle().equals("SUCCESS"))
                throw new Exception();
            Cookie cookie = new Cookie("JWT", messageDto.getText());
            response.addCookie(cookie);
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            if (statusCode == 401) {
                model.addAttribute("errorTitle", "401 ERROR");
                model.addAttribute("timestamp", new Date().toString());
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("errorTitle", messageDto.getTitle());
            model.addAttribute("timestamp", new Date().toString());
            e.printStackTrace();
        }

        return "redirect:/token";
    }
}
