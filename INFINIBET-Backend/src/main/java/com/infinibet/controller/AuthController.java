package com.infinibet.controller;

import com.infinibet.payload.*;
import com.infinibet.repository.PersonRepo;
import com.infinibet.security.jwt.JWTTokenProvider;
import com.infinibet.service.AuthService;
import com.infinibet.util.PersonUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PersonRepo personRepo;
    private final JWTTokenProvider tokenProvider;
    private final AuthService authService;
    private final PersonUtil personUtil;

    public AuthController(AuthenticationManager authenticationManager, PersonRepo personRepo, AuthService authService,
                          JWTTokenProvider tokenProvider, PersonUtil personUtil) {
        this.authenticationManager = authenticationManager;
        this.personRepo = personRepo;
        this.authService = authService;
        this.tokenProvider = tokenProvider;
        this.personUtil = personUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication, false);
        return ResponseEntity.ok(new JwtAuthResponse(jwt));
    }

    @PostMapping("/signup")
    public ApiResponse registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        authService.signup(signUpRequest.getUsername(), signUpRequest.getPassword());

        return new SuccessResponse("User registered");
    }

    @PostMapping("/change-password")
    public ApiResponse changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(personUtil.getCurrentPerson(), request.getOldPassword(), request.getNewPassword());

        return new SuccessResponse("Password changed");
    }

    @PostMapping("/checkUsernameAvailability")
    public UsernameAvailabilityResponse checkUsernameAvailability(@RequestBody UsernameAvailabilityRequest request) {
        return new UsernameAvailabilityResponse(!personRepo.findByUsername(request.getUsername()).isPresent());
    }
}
