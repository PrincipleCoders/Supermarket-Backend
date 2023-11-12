package com.principlecoders.userservice.controllers;

import com.principlecoders.common.dto.AdditionalDataDto;
import com.principlecoders.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @PutMapping("/additionalData")
    public ResponseEntity<?> updateAdditionalData(@RequestBody AdditionalDataDto additionalData) {
        return userService.updateAdditionalData(additionalData);
    }
}
