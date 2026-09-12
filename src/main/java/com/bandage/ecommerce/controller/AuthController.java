package com.bandage.ecommerce.controller;

import com.bandage.ecommerce.dto.SignupRequest;
import com.bandage.ecommerce.dto.UserResponse;
import com.bandage.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {

        UserResponse response =
                authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}