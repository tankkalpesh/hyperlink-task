package com.hyperlink.tmp.auth.service;

import com.hyperlink.tmp.auth.dto.LoginRequest;
import com.hyperlink.tmp.auth.dto.LoginResponse;
import com.hyperlink.tmp.auth.dto.RegisterRequest;
import com.hyperlink.tmp.auth.dto.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
    UserResponse getById(java.util.UUID id);
    java.util.List<UserResponse> listAll();
}
