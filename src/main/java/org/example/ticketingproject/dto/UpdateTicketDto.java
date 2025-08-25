package org.example.ticketingproject.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UpdateTicketDto {
    @Size(min = 5, max = 200, message = "Subject must be between 5 and 200 characters")
    private String subject;

    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    private String status;
    private String priority;
    private Long assigneeId;
}
