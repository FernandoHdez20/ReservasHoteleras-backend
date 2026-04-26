package com.fernando.auth.services;

import com.fernando.auth.dto.LoginRequest;
import com.fernando.auth.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}
