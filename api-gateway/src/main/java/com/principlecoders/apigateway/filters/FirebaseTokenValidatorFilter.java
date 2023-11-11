package com.principlecoders.apigateway.filters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class FirebaseTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null) {
            try {
                FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
                Authentication authentication = createAuthenticationFromToken(firebaseToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (FirebaseAuthException e) {
                // Handle token verification failure (e.g., log, respond with an error)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // Extract and return the JWT token from the request headers, cookies, or other sources.
        // For example, if the token is in the "Authorization" header:
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    private Authentication createAuthenticationFromToken(FirebaseToken firebaseToken) {
        Collection<? extends GrantedAuthority> authorities = getAuthoritiesFromClaims(firebaseToken.getClaims());
        return new UsernamePasswordAuthenticationToken(firebaseToken, null, authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesFromClaims(Map<String, Object> claims) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (claims.containsKey("role")) {
            String role = (String) claims.get("role");
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
