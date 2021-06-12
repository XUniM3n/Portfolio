package com.market.controller;

import com.market.form.ProductAddForm;
import com.market.model.Order;
import com.market.model.OrderStatus;
import com.market.model.Product;
import com.market.model.User;
import com.market.repository.CityRepository;
import com.market.repository.OrderRepository;
import com.market.repository.ProductRepository;
import com.market.repository.UserRepository;
import com.market.security.role.Role;
import com.market.service.AuthenticationService;
import com.market.service.FileStorageService;
import com.market.validator.ProductAddFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CityRepository cityRepository;
    private final OrderRepository orderRepository;
    private final FileStorageService fileStorageService;
    private final ProductAddFormValidator productAddFormValidator;

    @Autowired
    public ProductController(AuthenticationService authenticationService, UserRepository userRepository,
                             ProductRepository productRepository, CityRepository cityRepository,
                             OrderRepository orderRepository, FileStorageService fileStorageService,
                             ProductAddFormValidator productAddFormValidator) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cityRepository = cityRepository;
        this.orderRepository = orderRepository;
        this.fileStorageService = fileStorageService;
        this.productAddFormValidator = productAddFormValidator;
    }

    @GetMapping(value = "/add")
    public String addProductPage(Authentication authentication, Model model) {
        model.addAttribute("user", authenticationService.getUserByAuthentication(authentication));
        model.addAttribute("cities", cityRepository.findAll());
        return "product_add_page";
    }

    @PostMapping(value = "/add")
    public String addProduct(Authentication authentication, Model model,
                             @Valid @ModelAttribute("productAddForm") ProductAddForm productAddForm, RedirectAttributes attributes) {
        User user = authenticationService.getUserByAuthentication(authentication);
        model.addAttribute("user", user);
        Product product = Product.builder()
                .name(productAddForm.getName())
                .seller(user)
                .description(productAddForm.getDescription())
                .available(true)
                .price(productAddForm.getPrice())
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .city(cityRepository.findById(productAddForm.getCity()).get())
                .build();
        productRepository.save(product);
        attributes.addFlashAttribute("success", "Product added");
        return "redirect:/";
    }

    @PostMapping(value = "/order")
    public String buyProduct(Authentication authentication, @RequestParam(value = "id") Long id, RedirectAttributes attributes) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            attributes.addFlashAttribute("error", "No such product");
            return "redirect:/product/" + id;
        }
        Product product = productOptional.get();
        User user = authenticationService.getUserByAuthentication(authentication);
        if (product.getSeller().getId().equals(user.getId())) {
            attributes.addFlashAttribute("error", "It's not possible to order your own product.");
            return "redirect:/product/" + id;
        }
        Order order = Order.builder()
                .product(product)
                .price(product.getPrice())
                .buyer(user)
                .seller(product.getSeller())
                .dateStart(currentTime)
                .status(OrderStatus.OPEN)
                .build();
        orderRepository.saveAndFlush(order);
        return "redirect:/orders";
    }

    @PostMapping(value = "/remove")
    public String removeProduct(Authentication authentication, Model model, @RequestParam(value = "id") Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            model.addAttribute("error", "Invalid product");
        }
        Product product = productOptional.get();
        User user = authenticationService.getUserByAuthentication(authentication);

        if (!(product.getSeller().getId().equals(user.getId()) || (user.getRole().equals(Role.ADMIN)))) {
            model.addAttribute("error", "It's not possible to delete other people's products");
        }

        product.setAvailable(false);
        for (Order order : orderRepository.findAll().stream()
                .filter(o -> o.getProduct().getId().equals(product.getId())).collect(Collectors.toList())) {
            order.setStatus(OrderStatus.PRODUCT_REMOVED);
            order.setDateEnd(new Timestamp(System.currentTimeMillis()));
            orderRepository.saveAndFlush(order);
        }
        productRepository.saveAndFlush(product);

        model.addAttribute("message", "Product was successfullly removed");
        return "redirect:/";
    }


    @GetMapping("/{id}")
    public String productFull(Model model, Authentication authentication,
                              @PathVariable("id") Long id) {
        if (authentication != null) {
            model.addAttribute("user", authenticationService.getUserByAuthentication(authentication));
        }
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent() && product.get().isAvailable()) {
            model.addAttribute("product", product.get());
        } else {
            return "error1";
        }
        return "product_details_page";
    }

    @InitBinder("productAddForm")
    public void initUserFormValidator(WebDataBinder binder) {
        binder.addValidators(productAddFormValidator);
    }
}
