package com.Java24GroupProject.AirBnBPlatform.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

//This class is a part of the site security and handles creation of jwt tokens (used mainly by JwtAuthFilter class)
//requires various jwt dependencies, which are included in the in pom-file
@Component
public class JwtUtil {
    //both "${jwt.secret}" and "${jwt.expirationMs}" are references to values in the application.yml files (which should always be in git ignore, not pushed to GitHub)

    //secret authentication token
    @Value("${jwt.secret}")
    private String jwtSecret;

    //how long a token is valid
    //can be set to very long during testing/development, but should be shorter when actually going live
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    //method for creating an encrypted key for sign in
    private Key getSigninKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //generate a jwt token for a user
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                //subject for the token
                .setSubject(userDetails.getUsername())
                //validation token start time
                .setIssuedAt(new Date())
                //expiration (i.e., now + the expiration time length
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                //get key
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //validate a token
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    //extract username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    //check if token is expired
    private boolean isTokenExpired(String token) {
        //extract the expiration time
        Date expiration = extractAllClaims(token).getExpiration();
        //check if expiration is before current time
        return expiration.before(new Date());
    }

    //extract contents of a token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
