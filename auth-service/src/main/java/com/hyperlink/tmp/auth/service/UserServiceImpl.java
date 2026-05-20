package com.hyperlink.tmp.auth.service;

import com.hyperlink.tmp.auth.dto.LoginRequest;
import com.hyperlink.tmp.auth.dto.LoginResponse;
import com.hyperlink.tmp.auth.dto.RegisterRequest;
import com.hyperlink.tmp.auth.dto.UserResponse;
import com.hyperlink.tmp.auth.util.JwtUtil;
import com.hyperlink.tmp.auth.model.User;
import com.hyperlink.tmp.auth.repository.UserRepository;
import com.hyperlink.tmp.auth.util.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        Optional<User> existing = userRepository.findByEmail(registerRequest.getEmail());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setRole(Role.USER);
        user.setActive(true);

        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> existing = userRepository.findByEmail(loginRequest.getEmail());
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        User user = existing.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getId().toString(), user.getRole().name());
        return new LoginResponse(token);
    }

    @Override
    public UserResponse getById(java.util.UUID id) {
        return userRepository.findById(id)
                .map(UserResponse::from)
                .orElseThrow(() -> new java.util.NoSuchElementException("User not found"));
    }

    @Override
    public java.util.List<UserResponse> listAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }
}
