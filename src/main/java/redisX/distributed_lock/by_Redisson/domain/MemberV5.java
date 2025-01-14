package redisX.distributed_lock.by_Redisson.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor()
public class MemberV5 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketV5 ticket;

    public void setTicket(TicketV5 ticket) {
        this.ticket = ticket;
    }
}
