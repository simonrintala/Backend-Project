package com.Java24GroupProject.AirBnBPlatform.filters;

import com.Java24GroupProject.AirBnBPlatform.services.CustomUserDetailsService;
import com.Java24GroupProject.AirBnBPlatform.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//This class is a part of the site security and checks the validity of httpRequests to the site
//This run ONCE per HTTP-request (due to being extends OncePerRequestFilter)
//The filter validates the user via the jwt token
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    //constructor injection
    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    //filter method to validate HttpRequest, extracts the jwt token (from header or cookie) and validates it
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;

        //try to get the jwt from Authorization header
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            jwt = bearerToken.substring(7);
        }

        //if jwt not in header, check in cookies
        if (jwt == null && request.getCookies() != null) {
            //loop cookies and check for jwt
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        //if jwt has been found, try to authenticate user
        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                //extract username from token
                String username = jwtUtil.extractUsername(jwt);

                //get user details from db
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //validate token and create authentication (if token is valid)
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    //add request details for extra security
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // set authentication back into security context
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (JwtException exception) {
                logger.error("JWT validation failed\n", exception);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        //continue to next filter in filter change
        filterChain.doFilter(request, response);

    }
}
