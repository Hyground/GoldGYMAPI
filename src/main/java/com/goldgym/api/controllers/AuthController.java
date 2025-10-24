package com.goldgym.api.controllers;

import com.goldgym.api.dto.auth.LoginRequest;
import com.goldgym.api.dto.auth.LoginResponse;
import com.goldgym.api.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
    final String token = jwtUtil.generateToken(userDetails);
    
    // Extraer los roles del UserDetails
    List<String> roles = userDetails.getAuthorities().stream()
            .map(grantedAuthority -> grantedAuthority.getAuthority())
            .toList();

    // Retornar el token Y los roles
   return new LoginResponse(token, roles, usuario.getUsername());
}
}