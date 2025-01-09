package redisX.optimistic_lock.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import redisX.no_lock.domain.MemberV1;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor()
public class TicketV2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantity;
    @OneToMany(mappedBy = "ticket")
    private List<MemberV2> memberList;

    @Version // 낙관적 락을 위한 버전 필드 추가
    private Long version;

    public void decrease() {
        this.quantity -= 1;
    }

    public void addMember(MemberV2 member) {
        memberList.add(member);
        member.setTicket(this);
    }
}
