package com.principlecoders.common.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ApiSecretFilter extends OncePerRequestFilter {
    private final String SERVICE_API_SECRET;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String secret = request.getHeader("api-key");
        if (secret.equals(SERVICE_API_SECRET)) {
            chain.doFilter(request, response);
        } else {
            response.getWriter().write("Invalid API Key");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
        }
    }
}
