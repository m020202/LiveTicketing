package redisX.distributed_lock.by_Lettuce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.distributed_lock.by_Lettuce.domain.TicketV4;

public interface TicketRepositoryV4 extends JpaRepository<TicketV4, Long> {

}
