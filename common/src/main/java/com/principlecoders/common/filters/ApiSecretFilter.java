//package com.principlecoders.common.filters;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.principlecoders.common.utils.ServiceApiKeys.DELIVERY_API_KEY;
//import static com.principlecoders.common.utils.ServiceApiKeys.USER_API_KEY;
//import static com.principlecoders.common.utils.ServiceApiKeys.INVENTORY_API_KEY;
//import static com.principlecoders.common.utils.ServiceApiKeys.ORDER_API_KEY;
//
//@Service
//@RequiredArgsConstructor
//public class ApiSecretFilter extends OncePerRequestFilter {
//    List<String> secrets = Arrays.asList(DELIVERY_API_KEY, USER_API_KEY, INVENTORY_API_KEY, ORDER_API_KEY);
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        String secret = request.getHeader("api-key");
//        if (secrets.contains(secret)){
//            chain.doFilter(request, response);
//        } else {
//            response.getWriter().write("Invalid API Key");
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
//        }
//    }
//}
