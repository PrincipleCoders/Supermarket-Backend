package com.principlecoders.apigateway.controllers;

import com.principlecoders.common.dto.UserRoleDto;
import com.principlecoders.common.helpers.WebClientErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.principlecoders.common.utils.ServiceUrls.USER_URL;
import static com.principlecoders.common.utils.ServiceApiKeys.USER_API_KEY;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth/")
public class AuthController {
    private final WebClient webClient;
    private final WebClientErrorHandler webClientErrorHandler;

    @GetMapping("login")
    public Mono<?> validateLogin(@RequestHeader("Authorization") String accessToken) {
        String userUrl = USER_URL + "auth/login";

        return webClient.get()
                .uri(userUrl)
                .header("api-key", USER_API_KEY)
                .header("Authorization", accessToken)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }

    @PutMapping("user/role")
    public Mono<?> setUserRole(@RequestBody UserRoleDto userRole) {
        String userUrl = USER_URL + "auth/role";

        return webClient.put()
                .uri(userUrl)
                .header("api-key", USER_API_KEY)
                .bodyValue(userRole)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }


    @GetMapping("user/{userId}")
    public Mono<?> getUserAuthResult(@PathVariable String userId) {
        String userUrl = USER_URL + "auth/" + userId;

        return webClient.get()
                .uri(userUrl)
                .header("api-key", USER_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }
}