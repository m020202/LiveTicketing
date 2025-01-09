package redisX.no_lock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import redisX.no_lock.domain.MemberV1;

public interface MemberRepositoryV1 extends JpaRepository<MemberV1, Long> {
}
