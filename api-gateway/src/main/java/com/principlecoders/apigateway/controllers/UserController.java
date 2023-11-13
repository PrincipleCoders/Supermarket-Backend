package com.principlecoders.apigateway.controllers;

import com.principlecoders.common.dto.AdditionalDataDto;
import com.principlecoders.common.helpers.WebClientErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.principlecoders.common.utils.ServiceApiKeys.USER_API_KEY;
import static com.principlecoders.common.utils.ServiceUrls.USER_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final WebClient webClient;
    private final WebClientErrorHandler webClientErrorHandler;
    @GetMapping("/all")
    public Mono<?> getAllUsers() {
        String userUrl = USER_URL + "all";

        return webClient.get()
                .uri(userUrl)
                .header("api-key", USER_API_KEY)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }

    @PutMapping("/additionalData")
    public Mono<?> updateAdditionalData(@RequestBody AdditionalDataDto additionalData) {
        String userUrl = USER_URL + "user/all";

        return webClient.put()
                .uri(userUrl)
                .header("api-key", USER_API_KEY)
                .body(Mono.just(additionalData), AdditionalDataDto.class)
                .retrieve()
                .toEntity(Object.class)
                .onErrorResume(webClientErrorHandler::handle);
    }
}
