package com.crypto.coins.controller;

import com.crypto.coins.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public String createToken(@RequestParam String username) {
        return jwtUtil.generateToken(username);
    }
}
