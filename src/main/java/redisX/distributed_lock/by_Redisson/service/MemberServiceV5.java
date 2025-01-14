package redisX.distributed_lock.by_Redisson.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.distributed_lock.by_Redisson.domain.MemberV5;
import redisX.distributed_lock.by_Redisson.repository.MemberRepositoryV5;

@Service
@RequiredArgsConstructor
public class MemberServiceV5 {
    private final MemberRepositoryV5 memberRepository;
    @Transactional
    public Long join(String name) {
        MemberV5 member = MemberV5.builder().name(name).build();
        return memberRepository.save(member).getId();
    }

    public MemberV5 findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}
