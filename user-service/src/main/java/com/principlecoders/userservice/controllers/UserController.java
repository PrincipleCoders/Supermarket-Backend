package com.principlecoders.userservice.controllers;

import com.principlecoders.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/")
public class UserController {
    private final UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }
}
