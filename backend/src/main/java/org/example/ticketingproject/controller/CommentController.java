package org.example.ticketingproject.controller;

import org.example.ticketingproject.dto.CommentDto;
import org.example.ticketingproject.dto.CreateCommentDto;
import org.example.ticketingproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CreateCommentDto createCommentDto,
                                                   @RequestParam Long authorId) {
        CommentDto createdComment = commentService.createComment(createCommentDto, authorId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }
    
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<CommentDto>> getCommentsByTicketId(@PathVariable Long ticketId) {
        List<CommentDto> comments = commentService.getCommentsByTicketId(ticketId);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/ticket/{ticketId}/page")
    public ResponseEntity<Page<CommentDto>> getCommentsByTicketIdPaged(
            @PathVariable Long ticketId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CommentDto> comments = commentService.getCommentsByTicketId(ticketId, pageable);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<CommentDto>> getCommentsByAuthorId(@PathVariable Long authorId) {
        List<CommentDto> comments = commentService.getCommentsByAuthorId(authorId);
        return ResponseEntity.ok(comments);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id,
                                                   @RequestParam String body,
                                                   @RequestParam Long authorId) {
        CommentDto updatedComment = commentService.updateComment(id, body, authorId);
        return ResponseEntity.ok(updatedComment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestParam Long authorId) {
        commentService.deleteComment(id, authorId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/ticket/{ticketId}/count")
    public ResponseEntity<Long> getCommentCountByTicketId(@PathVariable Long ticketId) {
        long count = commentService.getCommentCountByTicketId(ticketId);
        return ResponseEntity.ok(count);
    }
}
