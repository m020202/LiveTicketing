package redisX.optimistic_lock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.optimistic_lock.domain.MemberV2;

public interface MemberRepositoryV2 extends JpaRepository<MemberV2, Long> {
}
