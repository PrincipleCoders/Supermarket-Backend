package com.principlecoders.userservice.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.principlecoders.common.dto.ResponseMessage;
import com.principlecoders.common.utils.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final FirebaseAuth firebaseAuth;
    public ResponseEntity<?> validateLogin(String accessToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(accessToken);
            String uid = decodedToken.getUid();
            UserRecord userRecord = firebaseAuth.getUser(uid);
            Object userRoles = userRecord.getCustomClaims().get("role");
            if (userRoles == null) {
                userRoles = setUserRole(uid, UserRoles.CUSTOMER).getHeaders().get("Role");
            }
            boolean emailVerified = userRecord.isEmailVerified();
            if (!emailVerified){
                firebaseAuth.generateEmailVerificationLink(userRecord.getEmail());
            }
            String newToken = firebaseAuth.createCustomToken(uid);

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .header("Authorization", "Bearer "+newToken)
                    .body(
                            new Object() {
                                final Object user = userRecord;
                                final boolean isEmailVerified = emailVerified;
                            }
                    );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            ResponseMessage.builder()
                                    .message("Login failed")
                                    .error(e.getMessage())
                                    .build()
                    );
        }
    }

    public ResponseEntity<?> setUserRole(String uid, UserRoles role) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("Role", role);
            firebaseAuth.setCustomUserClaims(uid, claims);

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .header("Role", String.valueOf(role))
                    .body(
                            ResponseMessage.builder()
                                    .message("Role set successful")
                                    .build()
                    );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(
                            ResponseMessage.builder()
                                    .message("Role set failed")
                                    .error(e.getMessage())
                                    .build()
                    );
        }
    }

    public ResponseEntity<?> validateToken(String token) {
        try {
            firebaseAuth.verifyIdToken(token);

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .header("Authorization", "Bearer "+token)
                    .body(
                            ResponseMessage.builder()
                                    .message("Token validated successfully")
                                    .build()
                    );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            ResponseMessage.builder()
                                    .message("Token validation failed")
                                    .error(e.getMessage())
                                    .build()
                    );
        }
    }
}
