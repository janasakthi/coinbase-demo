package com.crypto.coins.controller;

import com.crypto.coins.dto.AuthToken;
import com.crypto.coins.service.AuthService;
import com.crypto.coins.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/authenticate")
    public AuthToken createToken(@RequestParam String username) {
        return authService.authenticate(username);
    }
}
