package redisX.optimistic_lock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.no_lock.domain.MemberV1;
import redisX.no_lock.domain.TicketV1;
import redisX.no_lock.repository.TicketRepositoryV1;
import redisX.no_lock.service.MemberServiceV1;
import redisX.optimistic_lock.domain.MemberV2;
import redisX.optimistic_lock.domain.TicketV2;
import redisX.optimistic_lock.repository.TicketRepositoryV2;

@Service
@RequiredArgsConstructor
public class TicketServiceV2 {
    private final TicketRepositoryV2 ticketRepository;
    private final MemberServiceV2 memberService;

    @Transactional
    public Long create(Long quantity) {
        TicketV2 ticket = TicketV2.builder().quantity(10L).build();
        ticketRepository.save(ticket);
        return ticket.getId();
    }

    @Transactional
    public boolean ticketingByOptimisticLock(Long id) {
        TicketV2 ticket = ticketRepository.findById(id).orElseThrow();
        if (ticket.getQuantity() == 0) {
            return false;
        }
        ticket.getQuantity();
        ticket.decrease();
        ticketRepository.save(ticket);
        return true;
    }

    public TicketV2 findById(Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void addMember(Long id, Long memberId) {
        MemberV2 member = memberService.findById(memberId);
        TicketV2 ticket = findById(id);
        ticket.addMember(member);
        ticketRepository.save(ticket);
    }
}