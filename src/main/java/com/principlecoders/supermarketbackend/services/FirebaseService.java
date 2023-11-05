package com.principlecoders.supermarketbackend.services;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {
    private final FirebaseAuth firebaseAuth;

    @Autowired
    public FirebaseService(FirebaseApp firebaseApp) {
        this.firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
    }
}

