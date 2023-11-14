package com.principlecoders.userservice.services;

import com.google.firebase.auth.*;
import com.principlecoders.common.dto.AdditionalDataDto;
import com.principlecoders.common.dto.AuthResultDto;
import com.principlecoders.common.dto.UserDto;
import com.principlecoders.common.utils.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final FirebaseAuth firebaseAuth;

    public ResponseEntity<?> updateAdditionalData(AdditionalDataDto additionalData) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("address", additionalData.getAddress());
        claims.put("telephone", additionalData.getPhone());
        try {
            firebaseAuth.setCustomUserClaims(additionalData.getUserId(), claims);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(claims);
        } catch (FirebaseAuthException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    public ResponseEntity<?> getAllUsers() {
        try {
            ListUsersPage users = firebaseAuth.listUsers(null);

            List<AuthResultDto> authResultDtos = new ArrayList<>();
            users.getValues().forEach(userRecord -> {
                String address = String.valueOf(userRecord.getCustomClaims().get("address"));
                String telephone = String.valueOf(userRecord.getCustomClaims().get("telephone"));
                String role = String.valueOf(userRecord.getCustomClaims().get("role"));

                AuthResultDto authResultDto = AuthResultDto.builder()
                        .id(userRecord.getUid())
                        .name(userRecord.getDisplayName())
                        .email(userRecord.getEmail())
                        .telephone(telephone)
                        .address(address)
                        .role(UserRoles.valueOf(role))
                        .isEmailVerified(userRecord.isEmailVerified())
                        .imageUrl(userRecord.getPhotoUrl())
                        .build();

                authResultDtos.add(authResultDto);
            });
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authResultDtos);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    public ResponseEntity<?> getUserById(String userId) {
        try {
            UserRecord user = firebaseAuth.getUser(userId);
            String address = String.valueOf(user.getCustomClaims().get("address"));
            String telephone = String.valueOf(user.getCustomClaims().get("telephone"));

            UserDto userDto = UserDto.builder()
                    .id(userId)
                    .name(user.getDisplayName())
                    .email(user.getEmail())
                    .telephone(telephone)
                    .address(address)
                    .imageUrl(user.getPhotoUrl())
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userDto);
        }
        catch (FirebaseAuthException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
