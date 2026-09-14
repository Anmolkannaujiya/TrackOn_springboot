package com.example.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService) {

        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        //no jwt present
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            String email = jwtService.extractEmail(token);

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
               UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

               if(jwtService.isTokenValid(token,userDetails)){
                   UsernamePasswordAuthenticationToken authentication =
                           new UsernamePasswordAuthenticationToken(
                                   userDetails,
                                   null,
                                   userDetails.getAuthorities()
                           );
                   authentication.setDetails(
                     new WebAuthenticationDetailsSource()
                             .buildDetails(request)
                   );

                   SecurityContextHolder.getContext().setAuthentication(authentication);
               }
            }

        } catch(Exception e){
            //invalid or expired token
            //it will leave unauthenticated
        }

        filterChain.doFilter(request, response);
    }
}