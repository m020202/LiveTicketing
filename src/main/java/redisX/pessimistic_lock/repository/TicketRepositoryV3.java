package redisX.pessimistic_lock.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import redisX.pessimistic_lock.domain.TicketV3;

import java.util.Optional;

public interface TicketRepositoryV3 extends JpaRepository<TicketV3, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE) // 메서드 실행 시 비관적 락 적용
    @Query("SELECT t FROM TicketV3 t where t.id = :id")
    Optional<TicketV3> findByIdWithPessimisticLock(Long id);
}
