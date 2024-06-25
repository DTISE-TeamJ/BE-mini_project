package com.example.BE_mini_project.authentication.configuration;

import com.example.BE_mini_project.authentication.repository.BlacklistAuthRedisRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;


@Component
public class JwtCookieFilter extends OncePerRequestFilter {
    private final JwtDecoder jwtDecoder;
    private final BlacklistAuthRedisRepository blacklistAuthRedisRepository;

    public JwtCookieFilter(JwtDecoder jwtDecoder, BlacklistAuthRedisRepository blacklistAuthRedisRepository) {
        this.jwtDecoder = jwtDecoder;
        this.blacklistAuthRedisRepository = blacklistAuthRedisRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt)) {
            if (blacklistAuthRedisRepository.isTokenBlacklisted(jwt)) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
                return;
            }

            try {
                Jwt decodedJwt = jwtDecoder.decode(jwt);
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(decodedJwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}