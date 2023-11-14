package com.principlecoders.userservice.controllers;

import com.principlecoders.common.dto.AdditionalDataDto;
import com.principlecoders.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/")
public class UserController {
    private final UserService userService;

    @PutMapping("user")
    public ResponseEntity<?> updateAdditionalData(@RequestBody AdditionalDataDto additionalData) {
        return userService.updateAdditionalData(additionalData);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }
}
