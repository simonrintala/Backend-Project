package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.DTOs.AuthenticationRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.AuthenticationResponse;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserRequest;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.services.UserService;
import com.Java24GroupProject.AirBnBPlatform.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

//only handles registration of new users and login of users only
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    //constructor injection
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    //register a new user, uses the RegisterRequest DTO
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRequest));
    }

    //login as an existing user, uses the AuthenticationRequest and AuthenticationResponse DTOs
    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        try {
            //authenticate user login info
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            //set authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //get UserDetails
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            //generate jwt token for the login session
            String jwt = jwtUtil.generateToken(userDetails);
            ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                    //prevents hijacking of cookie
                    .httpOnly(true)
                    //set to false during development, NB!! should be TRUE when site goes live
                    .secure(false)
                    //cookie is available all paths in application
                    .path("/")
                    //cookie is valid for 10h
                    .maxAge(10*60*60)
                    //has three options, None, Lax and Strict - dictates strictness on request (Strict = cookie origin must be from the same site as it is used for)
                    .sameSite("Strict")
                    .build();

            //create response entity w. message and user info
            AuthenticationResponse authResponse = new AuthenticationResponse(
                    "login successful",
                    userDetails.getUsername(),
                    userService.findByUsername(userDetails.getUsername()).getRoles()
            );

            //return response with cookie header and body (necessary for secure user login session)
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(authResponse);

        //if username and/or password is not correct, return Http status unauthorized
        } catch (AuthenticationException exception) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // skapa en utgången cookie för att ersätta den befintliga jwt cookien
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false) // VIKTIGT! ändra i production
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        // response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        // rensa securitykontexten
        SecurityContextHolder.clearContext();

        // returnera svar med utgången cookie
        return ResponseEntity.ok()
                // hade kunnat ta bort denna raden och använda rad 134
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body("Logout successful!");
    }

    // kolla om en user är authenticated
    @GetMapping("/check")
    public ResponseEntity<?> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // kontrollera om användaren är authenticated
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated!");
        }

        // returnera user info om authentication
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(
                "Authenticated",
                user.getUsername(),
                user.getRoles()
        ));
    }
}
