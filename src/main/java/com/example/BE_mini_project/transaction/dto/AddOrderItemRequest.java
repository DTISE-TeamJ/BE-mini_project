package com.example.BE_mini_project.transaction.dto;

import lombok.Data;
import java.util.List;

@Data
public class AddOrderItemRequest {
    private Long userId;
    private List<TicketRequest> ticketRequests;

    @Data
    public static class TicketRequest {
        private Long ticketTypeId;
        private Integer quantity;
    }
}