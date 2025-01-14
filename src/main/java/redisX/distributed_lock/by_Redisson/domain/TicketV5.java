package redisX.distributed_lock.by_Redisson.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor()
public class TicketV5 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantity;
    @OneToMany(mappedBy = "ticket")
    private List<MemberV5> memberList;

    public void decrease() {
        this.quantity -= 1;
    }

    public void addMember(MemberV5 member) {
        memberList.add(member);
        member.setTicket(this);
    }
}
