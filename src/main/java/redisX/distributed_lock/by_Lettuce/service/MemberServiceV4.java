package redisX.distributed_lock.by_Lettuce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redisX.distributed_lock.by_Lettuce.domain.MemberV4;
import redisX.distributed_lock.by_Lettuce.repository.MemberRepositoryV4;

@Service
@RequiredArgsConstructor
public class MemberServiceV4 {
    private final MemberRepositoryV4 memberRepository;
    @Transactional
    public Long join(String name) {
        MemberV4 member = MemberV4.builder().name(name).build();
        return memberRepository.save(member).getId();
    }

    public MemberV4 findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}
