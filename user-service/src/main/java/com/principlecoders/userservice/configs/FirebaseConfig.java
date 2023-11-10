package com.principlecoders.userservice.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    public FirebaseApp initFirebaseApp() {
        try {
            FileInputStream serviceAccount = new FileInputStream("user-service/src/main/resources/firebaseKey.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp app = FirebaseApp.initializeApp(options);
            System.out.println("firebase initialized");
            return app;
        }
        catch (IOException e) {
            System.out.println("firebase error" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Bean
    public FirebaseAuth initFirebaseAuth() {
        return FirebaseAuth.getInstance(initFirebaseApp());
    }
}