package com.principlecoders.userservice.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.principlecoders.common.dto.AuthResultDto;
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
            String token = accessToken.split(" ")[1].trim();
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
            String uid = decodedToken.getUid();
            UserRecord userRecord = firebaseAuth.getUser(uid);

            String userRole = (String) userRecord.getCustomClaims().get("role");
            System.out.println(userRole);
            if (userRole == null) {
                userRole = setUserRole(uid, UserRoles.CUSTOMER).getHeaders().get("Role").get(0);
            }

            boolean emailVerified = userRecord.isEmailVerified();
//            if (!emailVerified){
//                firebaseAuth.generateEmailVerificationLink(userRecord.getEmail());
//            }

            String newToken = firebaseAuth.createCustomToken(uid);
            String address = (String) userRecord.getCustomClaims().get("address");
            String telephone = (String) userRecord.getCustomClaims().get("telephone");

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .header("Authorization", "Bearer "+newToken)
                    .body(AuthResultDto.builder()
                            .id(uid)
                            .email(userRecord.getEmail())
                            .name(userRecord.getDisplayName())
                            .role(UserRoles.valueOf(userRole))
                            .isEmailVerified(emailVerified)
                            .telephone(telephone)
                            .address(address)
                            .imageUrl(userRecord.getPhotoUrl())
                            .build()
                    );
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            ResponseMessage.builder()
                                    .message("Login failed")
                                    .error(e.getLocalizedMessage())
                                    .build()
                    );
        }
    }

    public ResponseEntity<?> setUserRole(String uid, UserRoles role) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", String.valueOf(role));
            firebaseAuth.setCustomUserClaims(uid, claims);

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(
                            ResponseMessage.builder()
                                    .message(role +" role set successful")
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
