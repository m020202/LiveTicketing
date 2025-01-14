package redisX.distributed_lock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.distributed_lock.domain.MemberV4;

public interface MemberRepositoryV4 extends JpaRepository<MemberV4, Long> {
}
