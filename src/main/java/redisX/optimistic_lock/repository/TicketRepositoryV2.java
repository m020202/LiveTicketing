package redisX.optimistic_lock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.optimistic_lock.domain.TicketV2;

public interface TicketRepositoryV2 extends JpaRepository<TicketV2, Long> {
}
