package redisX.no_lock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.no_lock.domain.MemberV1;
import redisX.no_lock.domain.TicketV1;
import redisX.no_lock.repository.TicketRepositoryV1;

@Service
@RequiredArgsConstructor
public class TicketServiceV1 {
    private final TicketRepositoryV1 ticketRepository;
    private final MemberServiceV1 memberService;

    @Transactional
    public Long create(Long quantity) {
        TicketV1 ticket = TicketV1.builder().quantity(10L).build();
        ticketRepository.save(ticket);
        return ticket.getId();
    }

    @Transactional
    public boolean ticketing(Long id){
        TicketV1 ticket = ticketRepository.findById(id).orElseThrow();
        if (ticket.getQuantity() == 0) return false;
        ticket.decrease();
        ticketRepository.saveAndFlush(ticket);
        return true;
    }

    public TicketV1 findById(Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void addMember(Long id, Long memberId) {
        MemberV1 member = memberService.findById(memberId);
        TicketV1 ticket = findById(id);
        ticket.addMember(member);
        ticketRepository.save(ticket);
    }
}
