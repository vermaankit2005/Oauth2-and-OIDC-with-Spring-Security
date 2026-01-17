package com.example.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        ClientRegistrationRepository clientRegistrationRepository) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/error").permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(Customizer.withDefaults())
                                .logout(logout -> logout
                                                .logoutSuccessHandler(oidcLogoutSuccessHandler(
                                                                clientRegistrationRepository)));
                return http.build();
        }

        private LogoutSuccessHandler oidcLogoutSuccessHandler(
                        ClientRegistrationRepository clientRegistrationRepository) {
                OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(
                                clientRegistrationRepository);
                oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
                return oidcLogoutSuccessHandler;
        }
}
