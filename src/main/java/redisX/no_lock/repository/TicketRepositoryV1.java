package redisX.no_lock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.no_lock.domain.TicketV1;

public interface TicketRepositoryV1 extends JpaRepository<TicketV1, Long> {

}
