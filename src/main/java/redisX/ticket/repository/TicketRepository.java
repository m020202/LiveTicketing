package redisX.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.ticket.domain.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
