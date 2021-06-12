package com.market.controller;

import com.market.form.UserProfileEditForm;
import com.market.model.City;
import com.market.model.Product;
import com.market.model.User;
import com.market.repository.CityRepository;
import com.market.repository.ProductRepository;
import com.market.repository.UserRepository;
import com.market.service.AuthenticationService;
import com.market.service.FileStorageService;
import com.market.validator.UserProfileEditFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainController {
    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CityRepository cityRepository;
    private final FileStorageService fileStorageService;
    private final UserProfileEditFormValidator userProfileEditFormValidator;

    @Autowired
    public MainController(AuthenticationService authenticationService, UserRepository userRepository, ProductRepository productRepository, CityRepository cityRepository, FileStorageService fileStorageService, UserProfileEditFormValidator userProfileEditFormValidator) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cityRepository = cityRepository;
        this.fileStorageService = fileStorageService;
        this.userProfileEditFormValidator = userProfileEditFormValidator;
    }


    @GetMapping(value = "/")
    public String listProducts(Model model, Authentication authentication,
                               @RequestParam(value = "city", required = false) Long cityId) {
        if (authentication != null) {
            User user = authenticationService.getUserByAuthentication(authentication);
            model.addAttribute("user", user);
        }

        Stream<Product> products = productRepository.findAll().stream().filter(Product::isAvailable);
        if (cityId != null) {
            Optional<City> cityOptional = cityRepository.findById(cityId);
            if (cityOptional.isPresent()) {
                products = products.filter(p -> p.getCity().equals(cityOptional.get()));
            }
        }

        model.addAttribute("products", products.collect(Collectors.toList()));
        model.addAttribute("cities", cityRepository.findAll());
        return "products_page";
    }

    @GetMapping("/profile")
    public String editProfilePage(Model model, Authentication authentication) {
        model.addAttribute("user", authenticationService.getUserByAuthentication(authentication));
        return "profile_edit_page";
    }

    @PostMapping("/profile")
    public String editProfile(Authentication authentication, @Valid @ModelAttribute("profileEditForm") UserProfileEditForm profileEditForm, RedirectAttributes attributes) {
        User user = authenticationService.getUserByAuthentication(authentication);
        user.setContacts(profileEditForm.getContacts());
        if (profileEditForm.isChangePassword()) {
            if (!passwordEncoder.encode(profileEditForm.getOldPassword()).equals(user.getPassword())) {
                attributes.addFlashAttribute("error", "Wrong old password");
                return "redirect:/profile";
            }
            user.setPassword(passwordEncoder.encode(profileEditForm.getPassword1()));
        }
        attributes.addFlashAttribute("success", "Successfully changed profile info");
        return "redirect:/profile";
    }

    @InitBinder("profileEditForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(userProfileEditFormValidator);
    }

//    @GetMapping("/error")
//    public String error(Exception e) {
//        e.printStackTrace();
//        return "error";
//    }

    @GetMapping("/images/{file-name:.+}")
    public void getFile(@PathVariable("file-name") String fileName,
                        HttpServletResponse response) {
        fileStorageService.writeFileToResponse(fileName, response);
    }

    @PostMapping("/images")
    public String handleFileUpload(HttpServletRequest request, @RequestParam("image") MultipartFile file, Authentication authentication) throws MalformedURLException {
        if (!authentication.isAuthenticated())
            return "redirect:/";
        URL referer = new URL(request.getHeader("referer"));
        Optional<Product> product = productRepository
                .findById(Long.parseLong(referer.getPath().replace("/product/", "")));
        if (product.isPresent()) {
            String filePath = fileStorageService.saveFile(file);
            product.get().setImage(filePath);
            productRepository.save(product.get());
        }
        return "redirect:" + referer.getPath();
    }
}
