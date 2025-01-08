package redisX.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import redisX.ticket.dto.TicketDTO;
import redisX.ticket.service.TicketService;

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/ticket")
    public Long create(@RequestBody TicketDTO.TicketRequestDTO request) {
        return ticketService.create(request.getQuantity());
    }

    @PatchMapping("/ticket/{id}")
    public String ticketing(@PathVariable("id") Long id) throws InterruptedException {
        boolean result = ticketService.ticketing(id);
        return result ? "성공!" : "실패ㅠㅠ";
    }
}
