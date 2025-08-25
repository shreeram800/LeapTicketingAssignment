package org.example.ticketingproject.entity;



import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "ticket_history",
        indexes = @Index(name = "idx_history_ticket", columnList = "ticket_id"))
@Data
public class TicketHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private User actor; // who performed the action

    @Enumerated(EnumType.STRING) @Column(length = 32)
    private TicketStatus fromStatus;

    @Enumerated(EnumType.STRING) @Column(length = 32)
    private TicketStatus toStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_assignee_id")
    private User fromAssignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_assignee_id")
    private User toAssignee;

    @Column(nullable = false)
    private Instant at; // timestamp

    @Column(length = 500)
    private String note;

    // getters/setters
}
