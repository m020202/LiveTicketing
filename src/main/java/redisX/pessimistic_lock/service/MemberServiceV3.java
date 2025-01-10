package redisX.pessimistic_lock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.no_lock.domain.MemberV1;
import redisX.no_lock.repository.MemberRepositoryV1;
import redisX.pessimistic_lock.domain.MemberV3;
import redisX.pessimistic_lock.repository.MemberRepositoryV3;

@Service
@RequiredArgsConstructor
public class MemberServiceV3 {
    private final MemberRepositoryV3 memberRepository;
    @Transactional
    public Long join(String name) {
        MemberV3 member = MemberV3.builder().name(name).build();
        return memberRepository.save(member).getId();
    }

    public MemberV3 findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}
