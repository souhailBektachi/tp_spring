package com.glsid.digital_banking.services;

import com.glsid.digital_banking.dtos.AuthenticationRequest;
import com.glsid.digital_banking.dtos.AuthenticationResponse;
import com.glsid.digital_banking.dtos.RegisterRequest;
import com.glsid.digital_banking.entities.Role;
import com.glsid.digital_banking.entities.User;
import com.glsid.digital_banking.repositories.RoleRepository;
import com.glsid.digital_banking.repositories.UserRepository;
import com.glsid.digital_banking.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Create the user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        
        // Assign roles
        Collection<Role> roles = new ArrayList<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            request.getRoles().forEach(roleName -> {
                Role role = roleRepository.findByName(roleName);
                if (role == null) {
                    role = new Role();
                    role.setName(roleName);
                    role = roleRepository.save(role);
                }
                roles.add(role);
            });
        } else {
            // Assign default role if none provided
            Role userRole = roleRepository.findByName("USER");
            if (userRole == null) {
                userRole = new Role();
                userRole.setName("USER");
                userRole = roleRepository.save(userRole);
            }
            roles.add(userRole);
        }
        
        user.setRoles(roles);
        userRepository.save(user);
        
        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
    
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
