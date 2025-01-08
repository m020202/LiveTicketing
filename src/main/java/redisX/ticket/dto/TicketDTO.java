package redisX.ticket.dto;

import lombok.Getter;
import lombok.Setter;

public class TicketDTO {
    @Setter
    @Getter
    public static class TicketRequestDTO {
        private Long quantity;
    }

    @Getter
    @Setter
    public static class TicketingDTO {
        private Long id;
        private Long quantity;
    }
}
