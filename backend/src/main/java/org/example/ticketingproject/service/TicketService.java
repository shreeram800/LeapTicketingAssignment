package org.example.ticketingproject.service;

import org.example.ticketingproject.dto.CreateTicketDto;
import org.example.ticketingproject.dto.TicketDto;
import org.example.ticketingproject.dto.UpdateTicketDto;
import org.example.ticketingproject.entity.Ticket;
import org.example.ticketingproject.entity.TicketPriority;
import org.example.ticketingproject.entity.TicketStatus;
import org.example.ticketingproject.entity.User;
import org.example.ticketingproject.repository.TicketRepository;
import org.example.ticketingproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public TicketDto createTicket(CreateTicketDto createTicketDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
            .orElseThrow(() -> new RuntimeException("Owner not found with id: " + ownerId));
        
        Ticket ticket = new Ticket();
        ticket.setCode(generateTicketCode());
        ticket.setSubject(createTicketDto.getSubject());
        ticket.setDescription(createTicketDto.getDescription());
        ticket.setOwner(owner);
        ticket.setStatus(TicketStatus.OPEN);
        
        if (createTicketDto.getPriority() != null) {
            try {
                ticket.setPriority(TicketPriority.valueOf(createTicketDto.getPriority().toUpperCase()));
            } catch (IllegalArgumentException e) {
                ticket.setPriority(TicketPriority.MEDIUM);
            }
        }
        
        if (createTicketDto.getAssigneeId() != null) {
            User assignee = userRepository.findById(createTicketDto.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee not found with id: " + createTicketDto.getAssigneeId()));
            ticket.setAssignee(assignee);
        }
        
        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDto(savedTicket);
    }
    
    public TicketDto getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
        return convertToDto(ticket);
    }
    
    public TicketDto getTicketByCode(String code) {
        Ticket ticket = ticketRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Ticket not found with code: " + code));
        return convertToDto(ticket);
    }
    
    public Page<TicketDto> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable).map(this::convertToDto);
    }
    
    public Page<TicketDto> getTicketsByOwner(Long ownerId, Pageable pageable) {
        return ticketRepository.findByOwnerId(ownerId, pageable).map(this::convertToDto);
    }
    
    public Page<TicketDto> getTicketsByAssignee(Long assigneeId, Pageable pageable) {
        return ticketRepository.findByAssigneeId(assigneeId, pageable).map(this::convertToDto);
    }
    
    public Page<TicketDto> getTicketsByStatus(TicketStatus status, Pageable pageable) {
        return ticketRepository.findByStatus(status, pageable).map(this::convertToDto);
    }
    
    public Page<TicketDto> getTicketsByPriority(TicketPriority priority, Pageable pageable) {
        return ticketRepository.findByPriority(priority, pageable).map(this::convertToDto);
    }
    
    public Page<TicketDto> searchTickets(String searchTerm, Pageable pageable) {
        return ticketRepository.searchTickets(searchTerm, pageable).map(this::convertToDto);
    }
    
    public TicketDto updateTicket(Long id, UpdateTicketDto updateTicketDto) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
        
        if (updateTicketDto.getSubject() != null) {
            ticket.setSubject(updateTicketDto.getSubject());
        }
        
        if (updateTicketDto.getDescription() != null) {
            ticket.setDescription(updateTicketDto.getDescription());
        }
        
        if (updateTicketDto.getStatus() != null) {
            try {
                TicketStatus status = TicketStatus.valueOf(updateTicketDto.getStatus().toUpperCase());
                ticket.setStatus(status);
                
                if (status == TicketStatus.CLOSED || status == TicketStatus.RESOLVED) {
                    ticket.setClosedAt(Instant.now());
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status: " + updateTicketDto.getStatus());
            }
        }
        
        if (updateTicketDto.getPriority() != null) {
            try {
                ticket.setPriority(TicketPriority.valueOf(updateTicketDto.getPriority().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid priority: " + updateTicketDto.getPriority());
            }
        }
        
        if (updateTicketDto.getAssigneeId() != null) {
            User assignee = userRepository.findById(updateTicketDto.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee not found with id: " + updateTicketDto.getAssigneeId()));
            ticket.setAssignee(assignee);
        }
        
        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDto(savedTicket);
    }
    
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }
    
    public void assignTicket(Long ticketId, Long assigneeId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));
        
        User assignee = userRepository.findById(assigneeId)
            .orElseThrow(() -> new RuntimeException("Assignee not found with id: " + assigneeId));
        
        ticket.setAssignee(assignee);
        ticketRepository.save(ticket);
    }
    
    public void changeTicketStatus(Long ticketId, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + ticketId));
        
        ticket.setStatus(status);
        
        if (status == TicketStatus.CLOSED || status == TicketStatus.RESOLVED) {
            ticket.setClosedAt(Instant.now());
        }
        
        ticketRepository.save(ticket);
    }
    
    private String generateTicketCode() {
        String prefix = "TKT";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 8);
    }
    
    private TicketDto convertToDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setCode(ticket.getCode());
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus().name());
        dto.setPriority(ticket.getPriority().name());
        dto.setOwner(convertUserToDto(ticket.getOwner()));
        dto.setAssignee(ticket.getAssignee() != null ? convertUserToDto(ticket.getAssignee()) : null);
        dto.setClosedAt(ticket.getClosedAt());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUpdatedAt(ticket.getUpdatedAt());
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
