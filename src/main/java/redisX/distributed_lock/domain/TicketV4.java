package redisX.distributed_lock.domain;

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
public class TicketV4 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantity;
    @OneToMany(mappedBy = "ticket")
    private List<MemberV4> memberList;

    public void decrease() {
        this.quantity -= 1;
    }

    public void addMember(MemberV4 member) {
        memberList.add(member);
        member.setTicket(this);
    }
}
