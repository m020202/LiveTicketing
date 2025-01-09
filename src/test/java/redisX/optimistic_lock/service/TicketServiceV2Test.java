package redisX.optimistic_lock.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import redisX.optimistic_lock.domain.TicketV2;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class TicketServiceV2Test {

    @Autowired
    private TicketServiceV2 ticketService;
    @Autowired
    private MemberServiceV2 memberService;
    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("낙관적 락 사용하여 테스트")
    void ticketingTestUsingOptimisticLock() throws InterruptedException {
        // given
        final Long TOTAL_TICKETS = 10L; // 총 10개의 티켓
        final int TOTAL_USERS = 20; // 20명이 동시에 구입하려고 시도
        final Long TICKET_ID = ticketService.create(TOTAL_TICKETS); // 테스트할 티켓 ID

        CountDownLatch countDownLatch = new CountDownLatch(TOTAL_USERS); // 20명 대기

        // when
        List<TicketServiceV2Test.TicketPurchaseWorkerV2> workers = Stream.generate(() -> new TicketPurchaseWorkerV2(memberService, ticketService, TICKET_ID, countDownLatch))
                .limit(TOTAL_USERS)
                .collect(Collectors.toList());

        workers.forEach(worker -> new Thread(worker).start());
        countDownLatch.await(); // 모든 작업이 완료될 때까지 대기

        // then
        TicketV2 updateTicket = ticketService.findById(TICKET_ID);
        assertThat(updateTicket.getQuantity()).isEqualTo(0);
    }

    private class TicketPurchaseWorkerV2 implements Runnable {
        private final MemberServiceV2 memberService;
        private final TicketServiceV2 ticketService;
        private final Long ticketId;
        private final CountDownLatch countDownLatch;

        public TicketPurchaseWorkerV2(MemberServiceV2 memberService, TicketServiceV2 ticketService, Long ticketId, CountDownLatch countDownLatch) {
            this.memberService = memberService;
            this.ticketService = ticketService;
            this.ticketId = ticketId;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            Long memberId = memberService.join("J");
            while (true) {
                try {
                    boolean result = ticketService.ticketingByOptimisticLock(ticketId);
                    if (result) {
                        System.out.println("구매 성공!");
                        ticketService.addMember(ticketId, memberId);
                    } else {
                        System.out.println("구매 실패!");
                    }
                    break;
                } catch (ObjectOptimisticLockingFailureException e) {
                    System.out.println("구매 실패!");
                }
            }
            countDownLatch.countDown();
        }
    }
}