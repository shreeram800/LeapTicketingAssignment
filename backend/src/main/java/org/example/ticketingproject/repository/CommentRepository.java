package org.example.ticketingproject.repository;

import org.example.ticketingproject.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByTicketIdOrderByCreatedAtDesc(Long ticketId);
    
    Page<Comment> findByTicketId(Long ticketId, Pageable pageable);
    
    List<Comment> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
    
    long countByTicketId(Long ticketId);
}
