package redisX.ticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.ticket.domain.Member;
import redisX.ticket.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Transactional
    public Long join(String name) {
        Member member = Member.builder().name(name).build();
        return memberRepository.save(member).getId();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}
