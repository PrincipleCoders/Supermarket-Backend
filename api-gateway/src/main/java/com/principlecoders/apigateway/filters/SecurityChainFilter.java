package com.principlecoders.apigateway.filters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

import static com.principlecoders.common.utils.SuperAdminKeys.SUPER_ADMIN_KEY;

@Service
public class SecurityChainFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Get the authorization header
        String token = request.getHeader("Authorization");

        // Check authorization header for null
        if (token == null) {
            // Reject the request if the authorization header is null
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Check if the request is to `/auth/login`
        if (request.getRequestURI().startsWith("/auth/login")) {
            // Allow the request to pass through
            if (token.startsWith("AccessToken ")) {
                chain.doFilter(request, response);
            }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Check if the request is to `/auth/setUserRole`
        if (request.getRequestURI().startsWith("/auth/setUserRole")) {
            // Validate the authorization header
            if (token.startsWith("Bearer ")) {
                // Validate the Firebase token
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                try {
                    firebaseAuth.verifyIdToken(token.substring(7));
                } catch (Exception e) {
                    // Reject the request if the Firebase token is invalid
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else if (token.startsWith("SuperadminKey ")) {
                // Validate the superadmin key
                if (!token.substring(14).equals(SUPER_ADMIN_KEY)) {
                    // Reject the request if the superadmin key is invalid
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } else {
                // Reject the request if the authorization header is invalid
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // Require the user to be authenticated for all other requests
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Redirect the user to the login page if they are not authenticated
            chain.doFilter(request, response);
            return;
        }

        // check and start a context if the token is valid
        if (token.startsWith("Bearer ")) {
            // Validate the Firebase token
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token.substring(7));
                String role = (String) decodedToken.getClaims().get("Role");
                Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
                authentication = new UsernamePasswordAuthenticationToken(token, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Reject the request if the Firebase token is invalid
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            chain.doFilter(request, response);
        }
    }
}
