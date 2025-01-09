package redisX.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.ticket.domain.Member;
import redisX.ticket.domain.Ticket;
import redisX.ticket.repository.TicketRepository;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final MemberService memberService;

    @Transactional
    public Long create(Long quantity) {
        Ticket ticket = Ticket.builder().quantity(10L).build();
        ticketRepository.save(ticket);
        return ticket.getId();
    }

    @Transactional
    public boolean ticketing(Long id){
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        if (ticket.getQuantity() == 0) return false;
        ticket.decrease();
        ticketRepository.saveAndFlush(ticket);
        return true;
    }

    @Transactional(rollbackFor = ObjectOptimisticLockingFailureException.class)
    public boolean ticketingByOptimisticLock(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        if (ticket.getQuantity() == 0) {
            return false;
        }
        ticket.getQuantity();
        ticket.decrease();
        ticketRepository.save(ticket);
        return true;
    }

    public Ticket findById(Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void addMember(Long id, Long memberId) {
        Member member = memberService.findById(memberId);
        Ticket ticket = findById(id);
        ticket.addMember(member);
        ticketRepository.save(ticket);
    }
}
