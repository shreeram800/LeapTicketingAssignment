package org.example.ticketingproject.repository;

import org.example.ticketingproject.entity.Ticket;
import org.example.ticketingproject.entity.TicketStatus;
import org.example.ticketingproject.entity.TicketPriority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    Optional<Ticket> findByCode(String code);
    
    boolean existsByCode(String code);
    
    Page<Ticket> findByOwnerId(Long ownerId, Pageable pageable);
    
    Page<Ticket> findByAssigneeId(Long assigneeId, Pageable pageable);
    
    Page<Ticket> findByStatus(TicketStatus status, Pageable pageable);
    
    Page<Ticket> findByPriority(TicketPriority priority, Pageable pageable);
    
    @Query("SELECT t FROM Ticket t WHERE t.owner.id = :userId OR t.assignee.id = :userId")
    Page<Ticket> findByUserInvolved(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT t FROM Ticket t WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:ownerId IS NULL OR t.owner.id = :ownerId) AND " +
           "(:assigneeId IS NULL OR t.assignee.id = :assigneeId)")
    Page<Ticket> findByFilters(
        @Param("status") TicketStatus status,
        @Param("priority") TicketPriority priority,
        @Param("ownerId") Long ownerId,
        @Param("assigneeId") Long assigneeId,
        Pageable pageable
    );
    
    @Query("SELECT t FROM Ticket t WHERE " +
           "t.subject LIKE %:searchTerm% OR " +
           "t.description LIKE %:searchTerm% OR " +
           "t.code LIKE %:searchTerm%")
    Page<Ticket> searchTickets(@Param("searchTerm") String searchTerm, Pageable pageable);
}
