package redisX.distributed_lock.by_Redisson.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.distributed_lock.by_Redisson.domain.MemberV5;

public interface MemberRepositoryV5 extends JpaRepository<MemberV5, Long> {
}
