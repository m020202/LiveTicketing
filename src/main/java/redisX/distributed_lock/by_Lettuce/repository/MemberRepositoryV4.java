package redisX.distributed_lock.by_Lettuce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.distributed_lock.by_Lettuce.domain.MemberV4;

public interface MemberRepositoryV4 extends JpaRepository<MemberV4, Long> {
}
