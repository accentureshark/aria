package com.accenture.aria.dto;

import com.accenture.aria.model.SprintStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintSummaryResponseDTO {

    private Long id;

    private String name;

    private SprintStatus status;

    private Integer totalTickets;

    private TicketStats ticketsByPriority;

    private TicketStats ticketsByStatus;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketStats {

        private Integer low;

        private Integer medium;

        private Integer high;

        private Integer urgent;
    }
}
