package com.example.BE_mini_project.events.controller;

import com.example.BE_mini_project.events.dto.EventsDTO;
import com.example.BE_mini_project.events.dto.TicketTypeDTO;
import com.example.BE_mini_project.events.service.TicketService;
import com.example.BE_mini_project.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/ticket-types")
public class TicketTypeController {

    private final TicketService ticketService;

    public TicketTypeController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<CustomResponse<List<TicketTypeDTO>>> getAllTicketTypes() {
        List<TicketTypeDTO> ticketTypes = ticketService.getAllTicketTypes();

        CustomResponse<List<TicketTypeDTO>> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "All tickets successfully retrieved",
                ticketTypes
        );

        return customResponse.toResponseEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<TicketTypeDTO>> getTicketTypeById(@PathVariable Long id) {
        TicketTypeDTO ticketType = ticketService.getTicketTypeById(id);

        CustomResponse<TicketTypeDTO> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Ticket by ID successfully retrieved",
                ticketType
        );

        return customResponse.toResponseEntity();
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<CustomResponse<List<TicketTypeDTO>>> getTicketTypesByEventId(@PathVariable Long eventId) {
        List<TicketTypeDTO> ticketTypes = ticketService.getTicketTypesByEventId(eventId);

        CustomResponse<List<TicketTypeDTO>> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Success",
                "Ticket by event successfully retrieved",
                ticketTypes
        );

        return customResponse.toResponseEntity();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<TicketTypeDTO>> updateTicketType(@PathVariable Long id, @RequestBody TicketTypeDTO ticketTypeDTO) {
        TicketTypeDTO updatedTicketType = ticketService.updateTicketType(id, ticketTypeDTO);

        CustomResponse<TicketTypeDTO> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Updated",
                "Ticket successfully updated",
                updatedTicketType
        );

        return customResponse.toResponseEntity();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<Void>> deleteTicketType(@PathVariable Long id) {
        ticketService.deleteTicketType(id);

        CustomResponse<Void> customResponse = new CustomResponse<>(
                HttpStatus.OK,
                "Deleted",
                "Ticket successfully deleted",
                null
        );

        return customResponse.toResponseEntity();
    }
}