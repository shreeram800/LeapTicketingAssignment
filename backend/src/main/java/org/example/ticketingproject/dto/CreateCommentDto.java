package org.example.ticketingproject.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateCommentDto {
    @NotBlank(message = "Comment body is required")
    private String body;

    @NotNull(message = "Ticket ID is required")
    private Long ticketId;
}
