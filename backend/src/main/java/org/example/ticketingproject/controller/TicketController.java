package org.example.ticketingproject.controller;

import org.example.ticketingproject.dto.CreateTicketDto;
import org.example.ticketingproject.dto.TicketDto;
import org.example.ticketingproject.dto.UpdateTicketDto;
import org.example.ticketingproject.entity.TicketPriority;
import org.example.ticketingproject.entity.TicketStatus;
import org.example.ticketingproject.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    
    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@Valid @RequestBody CreateTicketDto createTicketDto, 
                                                 @RequestParam Long ownerId) {
        TicketDto createdTicket = ticketService.createTicket(createTicketDto, ownerId);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        TicketDto ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<TicketDto> getTicketByCode(@PathVariable String code) {
        TicketDto ticket = ticketService.getTicketByCode(code);
        return ResponseEntity.ok(ticket);
    }
    
    @GetMapping
    public ResponseEntity<Page<TicketDto>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<TicketDto> tickets = ticketService.getAllTickets(pageable);
        return ResponseEntity.ok(tickets);
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Page<TicketDto>> getTicketsByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TicketDto> tickets = ticketService.getTicketsByOwner(ownerId, pageable);
        return ResponseEntity.ok(tickets);
    }
    
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<TicketDto>> getTicketsByAssignee(
            @PathVariable Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TicketDto> tickets = ticketService.getTicketsByAssignee(assigneeId, pageable);
        return ResponseEntity.ok(tickets);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TicketDto>> getTicketsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            TicketStatus ticketStatus = TicketStatus.valueOf(status.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<TicketDto> tickets = ticketService.getTicketsByStatus(ticketStatus, pageable);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<Page<TicketDto>> getTicketsByPriority(
            @PathVariable String priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            TicketPriority ticketPriority = TicketPriority.valueOf(priority.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<TicketDto> tickets = ticketService.getTicketsByPriority(ticketPriority, pageable);
            return ResponseEntity.ok(tickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<TicketDto>> searchTickets(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TicketDto> tickets = ticketService.searchTickets(searchTerm, pageable);
        return ResponseEntity.ok(tickets);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable Long id, 
                                                 @Valid @RequestBody UpdateTicketDto updateTicketDto) {
        TicketDto updatedTicket = ticketService.updateTicket(id, updateTicketDto);
        return ResponseEntity.ok(updatedTicket);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/assign/{assigneeId}")
    public ResponseEntity<Void> assignTicket(@PathVariable Long id, @PathVariable Long assigneeId) {
        ticketService.assignTicket(id, assigneeId);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Void> changeTicketStatus(@PathVariable Long id, @PathVariable String status) {
        try {
            TicketStatus ticketStatus = TicketStatus.valueOf(status.toUpperCase());
            ticketService.changeTicketStatus(id, ticketStatus);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
