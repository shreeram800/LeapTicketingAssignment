package org.example.ticketingproject.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class CommentDto {
    private Long id;
    private String body;
    private UserDto author;
    private Long ticketId;
    private Instant createdAt;
    private Instant updatedAt;
}
