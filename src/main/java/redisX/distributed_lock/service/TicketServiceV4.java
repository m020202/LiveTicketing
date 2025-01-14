package redisX.distributed_lock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.distributed_lock.domain.MemberV4;
import redisX.distributed_lock.domain.TicketV4;
import redisX.distributed_lock.repository.TicketRepositoryV4;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TicketServiceV4 {
    private final TicketRepositoryV4 ticketRepository;
    private final MemberServiceV4 memberService;
    private final RedisTemplate redisTemplate;

    @Transactional
    public Long create(Long quantity) {
        TicketV4 ticket = TicketV4.builder().quantity(10L).build();
        ticketRepository.save(ticket);
        return ticket.getId();
    }

    @Transactional
    public boolean ticketingBySpinLock(String key, Long id) throws InterruptedException {
        Boolean getKey;
        while (true){
            getKey = redisTemplate.opsForValue()
                    .setIfAbsent(String.valueOf(key), "lock", 300L, TimeUnit.MILLISECONDS);
            if (!getKey) {
                Thread.sleep(30);
            }
            else break;
        }

        if (!getKey) return false;

        try {
            TicketV4 ticket = ticketRepository.findById(id).orElseThrow();
            if (ticket.getQuantity() == 0) return false;
            ticket.decrease();
            ticketRepository.saveAndFlush(ticket);
            return true;
        } finally {
            redisTemplate.delete(String.valueOf(key));
        }
    }

    public TicketV4 findById(Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void addMember(Long id, Long memberId) {
        MemberV4 member = memberService.findById(memberId);
        TicketV4 ticket = findById(id);
        ticket.addMember(member);
        ticketRepository.save(ticket);
    }
}
