package org.example.ticketingproject.service;

import org.example.ticketingproject.dto.CreateUserDto;
import org.example.ticketingproject.dto.UserDto;
import org.example.ticketingproject.entity.Role;
import org.example.ticketingproject.entity.RoleName;
import org.example.ticketingproject.entity.User;
import org.example.ticketingproject.repository.RoleRepository;
import org.example.ticketingproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserDto createUser(CreateUserDto createUserDto) {
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new RuntimeException("User with email " + createUserDto.getEmail() + " already exists");
        }
        
        User user = new User();
        user.setFullName(createUserDto.getFullName());
        user.setEmail(createUserDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(createUserDto.getPassword()));
        user.setActive(true);
        
        // Set default role if none specified
        Set<Role> roles = new HashSet<>();
        if (createUserDto.getRoleNames() != null && !createUserDto.getRoleNames().isEmpty()) {
            roles = createUserDto.getRoleNames().stream()
                .map(roleName -> roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                    .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found")))
                .collect(Collectors.toSet());
        } else {
            // Default role
            Role defaultRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
            roles.add(defaultRole);
        }
        user.setRoles(roles);
        
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDto(user);
    }
    
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return convertToDto(user);
    }
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public List<UserDto> getActiveUsers() {
        return userRepository.findByActiveTrue().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public UserDto updateUser(Long id, CreateUserDto updateUserDto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        if (updateUserDto.getFullName() != null) {
            user.setFullName(updateUserDto.getFullName());
        }
        
        if (updateUserDto.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(updateUserDto.getPassword()));
        }
        
        if (updateUserDto.getRoleNames() != null && !updateUserDto.getRoleNames().isEmpty()) {
            Set<Role> roles = updateUserDto.getRoleNames().stream()
                .map(roleName -> roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                    .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found")))
                .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setActive(false);
        userRepository.save(user);
    }
    
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setActive(user.isActive());
        dto.setRoleNames(user.getRoles().stream()
            .map(role -> role.getName().name())
            .collect(Collectors.toSet()));
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}
