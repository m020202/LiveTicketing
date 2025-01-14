package redisX.distributed_lock.by_Redisson.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.distributed_lock.by_Redisson.domain.TicketV5;

public interface TicketRepositoryV5 extends JpaRepository<TicketV5, Long> {

}
