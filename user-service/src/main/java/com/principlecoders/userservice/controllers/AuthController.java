package com.principlecoders.userservice.controllers;

import com.principlecoders.common.dto.UserRoleDto;
import com.principlecoders.userservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user/")
public class AuthController {
    private final AuthService authService;

    @GetMapping("auth/login")
    public ResponseEntity<?> validateLogin(@RequestHeader("Authorization") String accessToken) {
        return authService.validateLogin(accessToken);
    }

    @PutMapping("auth/role")
    public ResponseEntity<?> setUserRole(@RequestBody UserRoleDto userRole) {
        return authService.setUserRole(userRole.getUserId(), userRole.getRole());
    }
}
