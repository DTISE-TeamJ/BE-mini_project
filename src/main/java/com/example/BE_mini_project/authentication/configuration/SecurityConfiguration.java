package com.example.BE_mini_project.authentication.configuration;

import jakarta.servlet.http.Cookie;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.example.BE_mini_project.authentication.util.RSAKeyProperties;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.BE_mini_project.authentication.repository.BlacklistAuthRedisRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Log
@Configuration
public class SecurityConfiguration {
    private final RSAKeyProperties keys;

    private final BlacklistAuthRedisRepository blacklistAuthRedisRepository;

    public SecurityConfiguration(RSAKeyProperties keys, BlacklistAuthRedisRepository blacklistAuthRedisRepository){
        this.blacklistAuthRedisRepository = blacklistAuthRedisRepository;
        this.keys = keys;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService detailsService){
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/auth/**").permitAll();
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/user/**").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers("/profile").authenticated();
                    auth.requestMatchers("/api/v1/events/create-event/**").hasRole("ADMIN"); // Restrict POST /events to ADMIN
                    auth.requestMatchers("/api/v1/events/edit-event/**").hasRole("ADMIN"); // Restrict PUT /events to ADMIN
                    auth.requestMatchers("/api/v1/events/delete-event/**").hasRole("ADMIN"); // Restrict DELETE /events to ADMIN
                    auth.requestMatchers("/api/v1/events/**").permitAll();
                    auth.requestMatchers("/api/v1/ticket-types/update/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/v1/ticket-types/delete/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/v1/ticket-types").permitAll();
                    auth.requestMatchers("/api/v1/ticket-types/**").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers("/api/v1/promos/update/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/v1/promos/delete/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/v1/promos").permitAll();
                    auth.requestMatchers("/api/v1/promos/**").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers("/api/v1/orders/add-item/**").hasRole("USER");
                    auth.requestMatchers("/api/v1/orders/adjust-quantity/**").hasRole("USER");
                    auth.requestMatchers("/api/v1/orders/process-payment/**").hasRole("USER");
                    auth.requestMatchers("/api/v1/orders/apply-promo/**").hasRole("USER");
                    auth.requestMatchers("/api/v1/orders/remove-promo/**").hasRole("USER");
                    auth.requestMatchers("/api/v1/orders/**").hasAnyRole("ADMIN", "USER");
                    auth.requestMatchers("/api/v1/users/**").hasRole("USER");
                    auth.requestMatchers("/api/v1/analytics/**").hasRole("ADMIN");
                    auth.requestMatchers("/api/v1/reviews/create/**").hasRole("USER");
                    auth.requestMatchers("/api/v1/reviews/delete/**").hasRole("USER");
                    auth.requestMatchers("/api/v1/reviews/**").permitAll();
                    auth.requestMatchers("/api/v1/event-categories/**").permitAll();

                    auth.anyRequest().authenticated();
                })
                .oauth2ResourceServer((oauth2) -> {
                    oauth2.jwt(jwt -> {
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                        jwt.decoder(jwtDecoder());
                    });
                    oauth2.bearerTokenResolver((request) -> {
                        Cookie[] cookies = request.getCookies();
                        if (cookies != null) {
                            for (Cookie cookie : cookies) {
                                if ("jwt".equals(cookie.getName())) {
                                    return cookie.getValue();
                                }
                            }
                        }
                        return null;
                    });
                })
                .addFilterAfter(new JwtCookieFilter(jwtDecoder(), blacklistAuthRedisRepository), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Add your frontend origin here
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtConverter;
    }
}
