package redisX.pessimistic_lock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.pessimistic_lock.domain.MemberV3;

public interface MemberRepositoryV3 extends JpaRepository<MemberV3, Long> {
}
