package com.example.BE_mini_project.events.service;

import com.example.BE_mini_project.events.dto.TicketTypeDTO;
import com.example.BE_mini_project.events.exception.ResourceNotFoundException;
import com.example.BE_mini_project.events.mapper.TicketTypeMapper;
import com.example.BE_mini_project.events.model.Events;
import com.example.BE_mini_project.events.model.TicketType;
import com.example.BE_mini_project.events.repository.TicketTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketTypeRepository ticketTypeRepository;
    private final TicketTypeMapper ticketTypeMapper;

    public TicketService(TicketTypeRepository ticketTypeRepository, TicketTypeMapper ticketTypeMapper) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketTypeMapper = ticketTypeMapper;
    }

    @Transactional
    public List<TicketType> createTicketTypes(List<TicketTypeDTO> ticketTypeDTOs, Events event) {
        List<TicketType> ticketTypes = ticketTypeDTOs.stream()
                .map(ticketTypeDTO -> {
                    TicketType ticketType = ticketTypeMapper.toEntity(ticketTypeDTO);
                    ticketType.setEvent(event);
                    return ticketType;
                })
                .collect(Collectors.toList());
        return ticketTypeRepository.saveAll(ticketTypes);
    }

    public List<TicketTypeDTO> getAllTicketTypes() {
        return ticketTypeRepository.findAll().stream()
                .map(ticketTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TicketTypeDTO getTicketTypeById(Long id) {
        TicketType ticketType = ticketTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TicketType not found with id: " + id));
        return ticketTypeMapper.toDTO(ticketType);
    }

    public List<TicketTypeDTO> getTicketTypesByEventId(Long eventId) {
        List<TicketType> ticketTypes = ticketTypeRepository.findByEventId(eventId);
        System.out.println("Number of ticket types found: " + ticketTypes.size());
        for (TicketType ticketType : ticketTypes) {
            System.out.println("TicketType: " + ticketType.getName() + ", Price: " + ticketType.getPrice() + ", Quantity: " + ticketType.getQuantity());
        }
        return ticketTypes.stream()
                .map(ticketTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TicketTypeDTO updateTicketType(Long id, TicketTypeDTO updatedTicketTypeDTO) {
        TicketType existingTicketType = ticketTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TicketType not found with id: " + id));

        if (updatedTicketTypeDTO.getName() != null && !updatedTicketTypeDTO.getName().trim().isEmpty()) {
            existingTicketType.setName(updatedTicketTypeDTO.getName());
        }
        if (updatedTicketTypeDTO.getPrice() != null && updatedTicketTypeDTO.getPrice() >= 0) {
            existingTicketType.setPrice(updatedTicketTypeDTO.getPrice());
        }
        if (updatedTicketTypeDTO.getQuantity() != null && updatedTicketTypeDTO.getQuantity() >= 0) {
            existingTicketType.setQuantity(updatedTicketTypeDTO.getQuantity());
        }

        TicketType updatedTicketType = ticketTypeRepository.save(existingTicketType);
        return ticketTypeMapper.toDTO(updatedTicketType);
    }

    public void deleteTicketType(Long id) {
        if (!ticketTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("TicketType not found with id: " + id);
        }
        ticketTypeRepository.deleteById(id);
    }

}
