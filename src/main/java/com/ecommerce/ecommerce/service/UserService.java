package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.DTOs.AuthRequest;
import com.ecommerce.ecommerce.DTOs.AuthResponse;
import com.ecommerce.ecommerce.DTOs.RegisterRequest;
import com.ecommerce.ecommerce.DTOs.UserDto;
import com.ecommerce.ecommerce.models.User;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.utils.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public UserDto registerUser(RegisterRequest request) {
        User user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);
        return mapEntityToDto(savedUser);
    }

    public AuthResponse authenticateUser(AuthRequest request) throws Exception {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        if (!auth.isAuthenticated()) {
            throw new Exception("User Not autheticated");
        }
        String token = jwtUtils.generateToken(request.getEmail());
        return AuthResponse
                .builder()
                .token(token)
                .build();
    }

    public User getLoggedInUserEmail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) return null;
        Optional<User> currentLoggedInUser = userRepository.findByEmail(userDetails.getUsername());
        return currentLoggedInUser.get();
    }

    private UserDto mapEntityToDto(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
