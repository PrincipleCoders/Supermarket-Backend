package com.principlecoders.userservice.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetUsersResult;
import com.google.firebase.auth.UserIdentifier;
import com.principlecoders.common.dto.AdditionalDataDto;
import com.principlecoders.common.dto.UserDto;
import com.principlecoders.common.utils.UserRoles;
import com.principlecoders.userservice.models.UserAdditionalData;
import com.principlecoders.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;

    public ResponseEntity<?> updateAdditionalData(AdditionalDataDto additionalData) {
        Optional<UserAdditionalData> userAdditionalData = userRepository.findById(additionalData.getId());
        if (userAdditionalData.isEmpty()) {
            UserAdditionalData data = UserAdditionalData.builder()
                    .id(additionalData.getId())
                    .address(additionalData.getAddress())
                    .phone(additionalData.getPhone())
                    .build();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userRepository.save(data));
        }
        else {
            UserAdditionalData data = UserAdditionalData.builder()
                    .id(additionalData.getId())
                    .address(additionalData.getAddress())
                    .phone(additionalData.getPhone())
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userRepository.save(data));
        }
    }

    public ResponseEntity<?> getAllUsers() {
        try {
            GetUsersResult users = firebaseAuth.getUsers(null);
            List<UserAdditionalData> usersData = userRepository.findAll();

            List<UserDto> usersDto = new ArrayList<>();
            users.getUsers().forEach(user -> {
                UserRoles role = UserRoles.valueOf(user.getCustomClaims().get("role").toString());
                Optional<UserAdditionalData> userData = usersData.stream().filter(data -> data.getId().equals(user.getUid())).findFirst();
                UserDto userDto = UserDto.builder()
                        .id(user.getUid())
                        .email(user.getEmail())
                        .telephone(user.getPhoneNumber())
                        .address(userData.isEmpty() ? "" : userData.get().getAddress())
                        .firstName(user.getDisplayName().split(" ")[0])
                        .lastName(user.getDisplayName().split(" ")[1])
                        .role(role)
                        .build();
                usersDto.add(userDto);
            });
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(usersDto);
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
