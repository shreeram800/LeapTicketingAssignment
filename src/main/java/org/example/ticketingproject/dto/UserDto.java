package org.example.ticketingproject.dto;

import lombok.Data;
import java.time.Instant;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private boolean active;
    private Set<String> roleNames;
    private Instant createdAt;
    private Instant updatedAt;
}
