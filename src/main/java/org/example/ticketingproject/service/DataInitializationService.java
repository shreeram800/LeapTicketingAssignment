package org.example.ticketingproject.service;

import org.example.ticketingproject.entity.Role;
import org.example.ticketingproject.entity.RoleName;
import org.example.ticketingproject.entity.User;
import org.example.ticketingproject.repository.RoleRepository;
import org.example.ticketingproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser();
    }
    
    private void initializeRoles() {
        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
    
    private void initializeAdminUser() {
        if (!userRepository.existsByEmail("admin@ticketing.com")) {
            User adminUser = new User();
            adminUser.setFullName("System Administrator");
            adminUser.setEmail("admin@ticketing.com");
            adminUser.setPasswordHash(passwordEncoder.encode("admin123"));
            adminUser.setActive(true);
            
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName(RoleName.ADMIN).orElse(null));
            adminUser.setRoles(adminRoles);
            
            userRepository.save(adminUser);
        }
    }
}
