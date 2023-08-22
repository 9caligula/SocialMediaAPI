package com.effectivemobile.socialMedia.service;

import com.effectivemobile.socialMedia.security.JwtService;
import com.effectivemobile.socialMedia.entity.User;
import com.effectivemobile.socialMedia.exception.UsernameAlreadyExistsException;
import com.effectivemobile.socialMedia.payload.request.AuthenticationRequest;
import com.effectivemobile.socialMedia.payload.request.RegisterRequest;
import com.effectivemobile.socialMedia.payload.response.AuthenticationResponse;
import com.effectivemobile.socialMedia.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder,
                                 JwtService jwtService, AuthenticationManager authenticationManager) {

        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Such a username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UsernameAlreadyExistsException("Such an email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with such a username: " + request.getUsername()));

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        if (!authenticate.isAuthenticated()) {
            throw new UsernameNotFoundException("Authentication failed, you may have entered an incorrect password");
        }

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<User> getUserByUsername(String username) {
        return new ResponseEntity<>(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with such a username: " + username)), HttpStatus.OK);
    }
}