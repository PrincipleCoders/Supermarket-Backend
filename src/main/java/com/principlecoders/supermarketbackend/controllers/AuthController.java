package com.principlecoders.supermarketbackend.controllers;

import com.principlecoders.supermarketbackend.dto.AuthCredentials;
import com.principlecoders.supermarketbackend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("auth/")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestParam String firstName,
                                          @RequestParam String lastName,
                                          @RequestParam String email,
                                          @RequestParam String password,
                                          @RequestPart MultipartFile profilePicture) {
        return ResponseEntity.ok(profilePicture.getOriginalFilename());
    }
}
