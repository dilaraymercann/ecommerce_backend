package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.SignupRequest;
import com.bandage.ecommerce.dto.UserResponse;
import com.bandage.ecommerce.entity.Role;
import com.bandage.ecommerce.entity.User;
import com.bandage.ecommerce.exception.EmailAlreadyExistsException;
import com.bandage.ecommerce.exception.ResourceNotFoundException;
import com.bandage.ecommerce.repository.RoleRepository;
import com.bandage.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email is already registered: " + request.getEmail()
            );
        }

        Role customerRole = roleRepository
                .findByName("CUSTOMER")
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "CUSTOMER role not found"
                        )
                );

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(customerRole)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().getName())
                .build();
    }
}