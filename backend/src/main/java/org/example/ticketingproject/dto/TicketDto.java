package org.example.ticketingproject.dto;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class TicketDto {
    private Long id;
    private String code;
    private String subject;
    private String description;
    private String status;
    private String priority;
    private UserDto owner;
    private UserDto assignee;
    private Instant closedAt;
    private List<CommentDto> comments;
    private Instant createdAt;
    private Instant updatedAt;
}
