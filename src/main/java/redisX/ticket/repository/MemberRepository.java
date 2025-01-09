package redisX.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.ticket.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
