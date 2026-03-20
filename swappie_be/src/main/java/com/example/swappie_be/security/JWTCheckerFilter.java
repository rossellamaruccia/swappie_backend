package com.example.swappie_be.security;

import com.example.swappie_be.Entities.User;
import com.example.swappie_be.Services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {
    private JWTTools jwtTools;
    private UserService userService;

    @Autowired
    public JWTCheckerFilter(JWTTools jwtTools, UserService userService) {
        this.jwtTools = jwtTools;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
        }
        String accessToken = authHeader.replace("Bearer ", "");
        try {
            jwtTools.verifyToken(accessToken);
            User authUser = this.userService.findById(jwtTools.getId(accessToken));
            Authentication authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token verification failed: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath())
                || "OPTIONS".equalsIgnoreCase(request.getMethod());
    }
}
