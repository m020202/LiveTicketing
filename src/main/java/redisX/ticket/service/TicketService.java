package redisX.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.ticket.domain.Ticket;
import redisX.ticket.repository.TicketRepository;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final SampleSpinLock sampleSpinLock;

    @Transactional
    public Long create(Long quantity) {
        Ticket ticket = Ticket.builder().quantity(10L).build();
        ticketRepository.save(ticket);
        return ticket.getId();

    }

    @Transactional
    public boolean ticketing(Long id) throws InterruptedException {
        if (sampleSpinLock.spinLock("ticket")) {
            Ticket ticket = ticketRepository.findById(id).orElseThrow();
            if (ticket.getQuantity() == 0) return false;
            ticket.decrease();
            ticketRepository.saveAndFlush(ticket);
            return true;
        }
        return false;
    }

    @Transactional
    public void redissonTicketing(Long id, Long quantity) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        ticket.decrease();
        ticketRepository.saveAndFlush(ticket);
    }
}
