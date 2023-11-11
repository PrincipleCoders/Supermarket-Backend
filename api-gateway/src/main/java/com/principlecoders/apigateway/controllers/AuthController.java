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
        String userUrl = USER_URL + "auth/login/" + accessToken;

        return webClient.get()
                .uri(userUrl)
                .header("api-key", USER_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }

    @PostMapping("setUserRole")
    public Mono<?> setUserRole(@RequestHeader("Authorization") String accessToken, @RequestBody UserRoleDto userRole) {
        String userUrl = USER_URL + "auth/setUserRole";

        return webClient.post()
                .uri(userUrl)
                .header("api-key", USER_API_KEY)
                .body(Mono.just(userRole), UserRoleDto.class)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }

}