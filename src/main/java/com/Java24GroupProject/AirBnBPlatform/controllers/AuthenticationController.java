package com.Java24GroupProject.AirBnBPlatform.controllers;

import com.Java24GroupProject.AirBnBPlatform.DTOs.AuthenticationRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.AuthenticationResponse;
import com.Java24GroupProject.AirBnBPlatform.DTOs.RegisterRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.RegisterResponse;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.services.UserService;
import com.Java24GroupProject.AirBnBPlatform.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

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

    //register a new user, uses the RegisterRequest and RegisterResponse DTOs
    //NB! currently only registers with username, password, email, phoneNr
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        //check if username already exists, and if is does, cast error
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists in database");
        }

        //same for email
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email already exists in database");
        }

        //same for phoneNr
        if (userService.existsByPhoneNr(registerRequest.getPhoneNr())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("PhoneNr already exists in database");
        }

        //maps the RegisterRequest to a User entity
        //NOTE: add address, description etc here also when class is expanded upon
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNr(registerRequest.getPhoneNr());
        user.setRoles(registerRequest.getRoles());

        //if no roles are provided in RegisterRequest, set to user
        if(registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        } else {
            user.setRoles(registerRequest.getRoles());
        }

        //register the user using in database UserService
        userService.registerUser(user);

        //create response object to return
        RegisterResponse registerResponse = new RegisterResponse(
                "user registered successfully",
                user.getUsername(),
                user.getRoles());

        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
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
}
