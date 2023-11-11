package com.principlecoders.userservice.controllers;

import com.principlecoders.common.dto.UserRoleDto;
import com.principlecoders.userservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login/{accessToken}")
    public ResponseEntity<?> validateLogin(@PathVariable String accessToken) {
        return authService.validateLogin(accessToken);
    }

    @PostMapping("/setUserRole")
    public ResponseEntity<?> setUserRole(@RequestBody UserRoleDto userRole) {
        return authService.setUserRole(userRole.getUserId(), userRole.getRole());
    }
}
