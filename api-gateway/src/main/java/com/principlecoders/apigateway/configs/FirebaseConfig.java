package com.principlecoders.apigateway.configs;

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
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream serviceAccount =
                        new FileInputStream("api-gateway/src/main/resources/firebaseKey.json");

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://principlecoders-ecommerce.firebaseio.com")
                        .build();

                return FirebaseApp.initializeApp(options);
            }
            else {
                return FirebaseApp.getInstance();
            }
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