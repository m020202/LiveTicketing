package redisX.pessimistic_lock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.no_lock.domain.MemberV1;
import redisX.no_lock.domain.TicketV1;
import redisX.no_lock.repository.TicketRepositoryV1;
import redisX.no_lock.service.MemberServiceV1;
import redisX.pessimistic_lock.domain.MemberV3;
import redisX.pessimistic_lock.domain.TicketV3;
import redisX.pessimistic_lock.repository.TicketRepositoryV3;

@Service
@RequiredArgsConstructor
public class TicketServiceV3 {
    private final TicketRepositoryV3 ticketRepository;
    private final MemberServiceV3 memberService;

    @Transactional
    public Long create(Long quantity) {
        TicketV3 ticket = TicketV3.builder().quantity(10L).build();
        ticketRepository.save(ticket);
        return ticket.getId();
    }

    @Transactional
    public boolean ticketingByPessimisticLock(Long id){
        TicketV3 ticket = ticketRepository.findByIdWithPessimisticLock(id).orElseThrow();
        if (ticket.getQuantity() == 0) return false;
        ticket.decrease();
        ticketRepository.saveAndFlush(ticket);
        return true;
    }

    public TicketV3 findById(Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void addMember(Long id, Long memberId) {
        MemberV3 member = memberService.findById(memberId);
        TicketV3 ticket = findById(id);
        ticket.addMember(member);
        ticketRepository.save(ticket);
    }
}