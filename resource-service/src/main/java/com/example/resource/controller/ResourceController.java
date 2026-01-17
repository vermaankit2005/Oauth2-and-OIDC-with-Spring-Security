package com.example.resource.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/api/data")
    public String getData(Authentication authentication) {
        return "Protected Data for user: " + ((JwtAuthenticationToken) authentication).getTokenAttributes().get("name")
                + " [Verified by Keycloak]";
    }
}
