package com.glsid.digital_banking.service;

import com.glsid.digital_banking.entities.Role;
import com.glsid.digital_banking.entities.User;
import com.glsid.digital_banking.repositories.RoleRepository;
import com.glsid.digital_banking.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService {
    
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public void initializeSecurityData() {
        log.info("Starting security data initialization...");
        
        try {
            // Initialize roles first
            Role adminRole = createRoleIfNotExists("ADMIN", "Administrator role with full access");
            Role userRole = createRoleIfNotExists("USER", "Standard user role");
            Role managerRole = createRoleIfNotExists("MANAGER", "Manager role with elevated access");
            Role tellerRole = createRoleIfNotExists("TELLER", "Bank teller role");
            
            // Initialize admin user
            createAdminUserIfNotExists(adminRole);
            
            log.info("Security data initialization completed successfully");
            
        } catch (Exception e) {
            log.error("Error during security data initialization", e);
            throw new RuntimeException("Failed to initialize security data", e);
        }
    }
    
    private Role createRoleIfNotExists(String roleName, String description) {
        Optional<Role> existingRole = roleRepository.findByName(roleName);
        
        if (existingRole.isPresent()) {
            log.debug("Role {} already exists, skipping creation", roleName);
            return existingRole.get();
        }
        
        Role role = Role.builder()
                .name(roleName)
                .description(description)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Role savedRole = roleRepository.save(role);
        log.info("Created new role: {}", roleName);
        return savedRole;
    }
    
    private void createAdminUserIfNotExists(Role adminRole) {
        String adminEmail = "admin@digitalbanking.com";
        String adminUsername = "admin"; // Ensure a unique username

        if (userRepository.findByEmail(adminEmail).isPresent()) {
            log.debug("Admin user already exists, skipping creation");
            return;
        }

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        User adminUser = User.builder()
                .username(adminUsername) // Set username
                .email(adminEmail)
                .firstName("System")
                .lastName("Administrator")
                .password(passwordEncoder.encode("admin123"))
                .emailVerified(true)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(adminRoles)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(adminUser);
        log.info("Created admin user: {}", adminEmail);
    }
}

// Reminder: Make sure DataInitializationService.initializeSecurityData() is called at application startup,
// e.g., from a @PostConstruct method in a configuration or main class.
