package com.principlecoders.userservice.services;

import com.principlecoders.common.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
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
