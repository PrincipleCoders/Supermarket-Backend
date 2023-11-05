package com.principlecoders.supermarketbackend.services;

import com.principlecoders.supermarketbackend.dto.AuthCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final FirebaseService firebaseService;

    @Autowired
    public AuthService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }
}
