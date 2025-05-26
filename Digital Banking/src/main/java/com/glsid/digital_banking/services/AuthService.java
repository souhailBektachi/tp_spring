package com.glsid.digital_banking.services;

import com.glsid.digital_banking.dtos.AuthenticationRequest;
import com.glsid.digital_banking.dtos.AuthenticationResponse;
import com.glsid.digital_banking.dtos.RegisterRequest;
import com.glsid.digital_banking.entities.Role;
import com.glsid.digital_banking.entities.User;
import com.glsid.digital_banking.repositories.RoleRepository;
import com.glsid.digital_banking.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final OAuth2JwtService oAuth2JwtService;
    
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Check if user already exists
        String username = request.getUsername();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User with username " + username + " already exists");
        }

        // Create new user
        User user = User.builder()
                .username(username)
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .emailVerified(false)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        // Assign default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        
        // Generate JWT token using OAuth2JwtService
        String jwtToken = oAuth2JwtService.generateToken(savedUser);
        String refreshToken = oAuth2JwtService.generateRefreshToken(savedUser);
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(oAuth2JwtService.getJwtExpiration())
                .build();
    }
    
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String username = request.getUsername();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update last login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        String jwtToken = oAuth2JwtService.generateToken(user);
        String refreshToken = oAuth2JwtService.generateRefreshToken(user);
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(oAuth2JwtService.getJwtExpiration())
                .build();
    }
    
    @Transactional
    public User registerUser(String email, String firstName, String lastName, String password) {
        // Check if user already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists");
        }
        
        // Create new user
        User user = new User();
        user.setEmail(email); // Changed from setUsername to setEmail
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true); // Changed from setActive to setEnabled
        user.setEmailVerified(false);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        // Assign default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found")); // Fixed Optional handling
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        
        return userRepository.save(user);
    }
    
    @Transactional
    public User registerAdmin(String email, String firstName, String lastName, String password) {
        // Check if user already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists");
        }
        
        // Create new admin user
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setEmailVerified(true); // Admin is pre-verified
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        // Assign ADMIN role
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found")); // Fixed Optional handling
        
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);
        
        return userRepository.save(user);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    
    @Transactional
    public User updateLastLogin(String username) {
        User user = findByUsername(username);
        user.setLastLoginAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @Transactional
    public void addRoleToUser(String username, String roleName) {
        User user = findByUsername(username);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        
        Set<Role> userRoles = new HashSet<>(user.getRoles()); // Fixed Set conversion
        userRoles.add(role);
        user.setRoles(userRoles);
        
        userRepository.save(user);
    }
    
    @Transactional
    public void removeRoleFromUser(String username, String roleName) {
        User user = findByUsername(username);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        
        Set<Role> userRoles = new HashSet<>(user.getRoles());
        userRoles.remove(role);
        user.setRoles(userRoles);
        
        userRepository.save(user);
    }
    
    public boolean hasRole(String username, String roleName) {
        User user = findByUsername(username);
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }
    
    @Transactional
    public void enableUser(String username) {
        User user = findByUsername(username);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public void disableUser(String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    public void verifyEmail(String username) {
        User user = findByUsername(username);
        user.setEmailVerified(true);
        userRepository.save(user);
    }
}
