package redisX.pessimistic_lock.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import redisX.pessimistic_lock.domain.TicketV3;


import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketServiceV3Test {
    @Autowired
    private TicketServiceV3 ticketServiceV3;
    @Autowired
    private MemberServiceV3 memberServiceV3;

    @Test
    @DisplayName("비관적 락 사용하여 테스트")
    void ticketingTestUsingOptimisticLock() throws InterruptedException {
        // given
        final Long TOTAL_TICKETS = 10L; // 총 10개의 티켓
        final int TOTAL_USERS = 20; // 20명이 동시에 구입하려고 시도
        final Long TICKET_ID = ticketServiceV3.create(TOTAL_TICKETS); // 테스트할 티켓 ID

        CountDownLatch countDownLatch = new CountDownLatch(TOTAL_USERS); // 20명 대기

        // when
        List<TicketServiceV3Test.TicketPurchaseWorkerV3> workers = Stream.generate(() -> new TicketServiceV3Test.TicketPurchaseWorkerV3(memberServiceV3, ticketServiceV3, TICKET_ID, countDownLatch))
                .limit(TOTAL_USERS)
                .collect(Collectors.toList());

        workers.forEach(worker -> new Thread(worker).start());
        countDownLatch.await(); // 모든 작업이 완료될 때까지 대기

        // then
        TicketV3 updateTicket = ticketServiceV3.findById(TICKET_ID);
        assertThat(updateTicket.getQuantity()).isEqualTo(0);
    }

    private class TicketPurchaseWorkerV3 implements Runnable {
        private final MemberServiceV3 memberService;
        private final TicketServiceV3 ticketService;
        private final Long ticketId;
        private final CountDownLatch countDownLatch;

        public TicketPurchaseWorkerV3(MemberServiceV3 memberService, TicketServiceV3 ticketService, Long ticketId, CountDownLatch countDownLatch) {
            this.memberService = memberService;
            this.ticketService = ticketService;
            this.ticketId = ticketId;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            Long memberId = memberService.join("J");
            boolean result = ticketService.ticketingByPessimisticLock(ticketId);
            if (result) {
                System.out.println("구매 성공!");
                ticketService.addMember(ticketId, memberId);
            }
             else {
                System.out.println("구매 실패!");
            }
            countDownLatch.countDown();
        }
    }
}