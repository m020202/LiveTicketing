package redisX.optimistic_lock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.no_lock.domain.MemberV1;
import redisX.no_lock.repository.MemberRepositoryV1;
import redisX.optimistic_lock.domain.MemberV2;
import redisX.optimistic_lock.repository.MemberRepositoryV2;

@Service
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final MemberRepositoryV2 memberRepository;
    @Transactional
    public Long join(String name) {
        MemberV2 member = MemberV2.builder().name(name).build();
        return memberRepository.save(member).getId();
    }

    public MemberV2 findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}