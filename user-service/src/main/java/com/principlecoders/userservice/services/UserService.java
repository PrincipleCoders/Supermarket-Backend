package com.principlecoders.userservice.services;

import com.principlecoders.common.dto.AdditionalDataDto;
import com.principlecoders.userservice.models.UserAdditionalData;
import com.principlecoders.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
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
}
