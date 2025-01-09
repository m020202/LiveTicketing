package redisX.optimistic_lock.domain;

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
public class MemberV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketV2 ticket;

    public void setTicket(TicketV2 ticket) {
        this.ticket = ticket;
    }
}
