package com.principlecoders.userservice.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GetUsersResult;
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
            firebaseAuth.setCustomUserClaims(additionalData.getId(), claims);
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
            GetUsersResult users = firebaseAuth.getUsers(null);

            List<AuthResultDto> authResultDtos = new ArrayList<>();
            users.getUsers().forEach(user -> {
                UserRoles role = UserRoles.valueOf(user.getCustomClaims().get("role").toString());
                String address = user.getCustomClaims().get("address").toString();
                String telephone = user.getCustomClaims().get("telephone").toString();

                AuthResultDto authResultDto = AuthResultDto.builder()
                        .id(user.getUid())
                        .email(user.getEmail())
                        .name(user.getDisplayName())
                        .role(role)
                        .isEmailVerified(user.isEmailVerified())
                        .address(address)
                        .telephone(telephone)
                        .imageUrl(user.getPhotoUrl())
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
        if (userId.equals("1")) {
            return ResponseEntity.ok(UserDto.builder()
                    .id("1")
                    .name("John Doe")
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
