package redisX.distributed_lock.by_Redisson.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.distributed_lock.by_Redisson.domain.MemberV5;
import redisX.distributed_lock.by_Redisson.domain.TicketV5;
import redisX.distributed_lock.by_Redisson.repository.TicketRepositoryV5;
import redisX.no_lock.domain.TicketV1;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TicketServiceV5 {
    private final TicketRepositoryV5 ticketRepository;
    private final MemberServiceV5 memberService;
    private final RedissonClient redissonClient;

    @Transactional
    public Long create(Long quantity) {
        TicketV5 ticket = TicketV5.builder().quantity(10L).build();
        ticketRepository.save(ticket);
        return ticket.getId();
    }

    @Transactional
    public boolean ticketing(Long id){
        TicketV5 ticket = ticketRepository.findById(id).orElseThrow();
        if (ticket.getQuantity() == 0) return false;
        ticket.decrease();
        ticketRepository.saveAndFlush(ticket);
        return true;
    }

    @Transactional
    public Boolean ticketingByDistributedLock(Long id) throws InterruptedException {
        String key = "LOCK";
        RLock rLock = redissonClient.getLock(key);
        try {
            boolean available = rLock.tryLock(5L, 3L, TimeUnit.SECONDS);
            if (!available) {
                return false;
            }
            return ticketing(id);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            rLock.unlock();
        }
    }

    public TicketV5 findById(Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void addMember(Long id, Long memberId) {
        MemberV5 member = memberService.findById(memberId);
        TicketV5 ticket = findById(id);
        ticket.addMember(member);
        ticketRepository.save(ticket);
    }
}
