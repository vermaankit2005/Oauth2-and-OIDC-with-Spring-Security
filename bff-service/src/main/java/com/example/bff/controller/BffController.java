package com.example.bff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
public class BffController {

    private final WebClient webClient;

    @GetMapping("/api/me")
    public String getProtectedData(Authentication authentication) {
        return webClient.get()
                .uri("http://localhost:8082/api/data")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @GetMapping("/")
    public String Welcome() {
        return "Welcome to BFF Service. Visit /api/me to fetch protected data.";
    }
}
