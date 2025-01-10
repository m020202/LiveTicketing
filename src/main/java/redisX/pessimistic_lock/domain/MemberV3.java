package redisX.pessimistic_lock.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import redisX.no_lock.domain.TicketV1;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor()
public class MemberV3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketV3 ticket;

    public void setTicket(TicketV3 ticket) {
        this.ticket = ticket;
    }
}