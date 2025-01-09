package redisX.no_lock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.no_lock.domain.MemberV1;
import redisX.no_lock.repository.MemberRepositoryV1;

@Service
@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepository;
    @Transactional
    public Long join(String name) {
        MemberV1 member = MemberV1.builder().name(name).build();
        return memberRepository.save(member).getId();
    }

    public MemberV1 findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}
