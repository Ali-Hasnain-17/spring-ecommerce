package com.ecommerce.ecommerce.controllers;

import com.ecommerce.ecommerce.DTOs.AuthRequest;
import com.ecommerce.ecommerce.DTOs.AuthResponse;
import com.ecommerce.ecommerce.DTOs.RegisterRequest;
import com.ecommerce.ecommerce.DTOs.UserDto;
import com.ecommerce.ecommerce.utils.JwtUtils;
import com.ecommerce.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    @PostMapping("/register")
    public UserDto registerUser(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/authenticate")
    public AuthResponse authenticateUser(@RequestBody AuthRequest request) throws Exception {
        return userService.authenticateUser(request);
    }
}
