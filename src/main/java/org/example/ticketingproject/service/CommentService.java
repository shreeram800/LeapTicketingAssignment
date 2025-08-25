package org.example.ticketingproject.service;

import org.example.ticketingproject.dto.CommentDto;
import org.example.ticketingproject.dto.CreateCommentDto;
import org.example.ticketingproject.entity.Comment;
import org.example.ticketingproject.entity.Ticket;
import org.example.ticketingproject.entity.User;
import org.example.ticketingproject.repository.CommentRepository;
import org.example.ticketingproject.repository.TicketRepository;
import org.example.ticketingproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public CommentDto createComment(CreateCommentDto createCommentDto, Long authorId) {
        Ticket ticket = ticketRepository.findById(createCommentDto.getTicketId())
            .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + createCommentDto.getTicketId()));
        
        User author = userRepository.findById(authorId)
            .orElseThrow(() -> new RuntimeException("Author not found with id: " + authorId));
        
        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setAuthor(author);
        comment.setBody(createCommentDto.getBody());
        
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }
    
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        return convertToDto(comment);
    }
    
    public List<CommentDto> getCommentsByTicketId(Long ticketId) {
        return commentRepository.findByTicketIdOrderByCreatedAtDesc(ticketId)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public Page<CommentDto> getCommentsByTicketId(Long ticketId, Pageable pageable) {
        return commentRepository.findByTicketId(ticketId, pageable).map(this::convertToDto);
    }
    
    public List<CommentDto> getCommentsByAuthorId(Long authorId) {
        return commentRepository.findByAuthorIdOrderByCreatedAtDesc(authorId)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public CommentDto updateComment(Long id, String newBody, Long authorId) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        
        // Check if the user is the author of the comment
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new RuntimeException("Only the author can update this comment");
        }
        
        comment.setBody(newBody);
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }
    
    public void deleteComment(Long id, Long authorId) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        
        // Check if the user is the author of the comment
        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new RuntimeException("Only the author can delete this comment");
        }
        
        commentRepository.deleteById(id);
    }
    
    public long getCommentCountByTicketId(Long ticketId) {
        return commentRepository.countByTicketId(ticketId);
    }
    
    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setBody(comment.getBody());
        dto.setTicketId(comment.getTicket().getId());
        dto.setAuthor(convertUserToDto(comment.getAuthor()));
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }
    
    private org.example.ticketingproject.dto.UserDto convertUserToDto(User user) {
        org.example.ticketingproject.dto.UserDto userDto = new org.example.ticketingproject.dto.UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setActive(user.isActive());
        return userDto;
    }
}
