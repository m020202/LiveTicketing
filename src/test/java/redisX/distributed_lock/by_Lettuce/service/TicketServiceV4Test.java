package redisX.distributed_lock.by_Lettuce.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redisX.distributed_lock.by_Lettuce.domain.TicketV4;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TicketServiceV4Test {
    @Autowired
    private TicketServiceV4 ticketService;
    @Autowired
    private MemberServiceV4 memberService;
    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("스핀락을 활용하여 테스트")
    void ticketingTest() throws InterruptedException {
        // given
        final Long TOTAL_TICKETS = 10L; // 총 10개의 티켓
        final int TOTAL_USERS = 20; // 20명이 동시에 구입하려고 시도
        final Long TICKET_ID = ticketService.create(TOTAL_TICKETS); // 테스트할 티켓 ID

        CountDownLatch countDownLatch = new CountDownLatch(TOTAL_USERS); // 20명 대기

        // when
        List<TicketServiceV4Test.TicketPurchaseWorker> workers = Stream.generate(() -> new TicketServiceV4Test.TicketPurchaseWorker(memberService, ticketService, TICKET_ID,countDownLatch))
                .limit(TOTAL_USERS)
                .collect(Collectors.toList());

        workers.forEach(worker -> new Thread(worker).start());
        countDownLatch.await(); // 모든 작업이 완료될 때까지 대기

        // then
        TicketV4 updateTicket = ticketService.findById(TICKET_ID);
        assertThat(updateTicket.getQuantity()).isEqualTo(0);
    }

    private class TicketPurchaseWorker implements Runnable {
        private final MemberServiceV4 memberService;
        private final TicketServiceV4 ticketService;
        private final Long ticketId;
        private final CountDownLatch countDownLatch;

        public TicketPurchaseWorker(MemberServiceV4 memberService, TicketServiceV4 ticketService, Long ticketId, CountDownLatch countDownLatch) {
            this.memberService = memberService;
            this.ticketService = ticketService;
            this.ticketId = ticketId;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            Long memberId = memberService.join("J");
            boolean result = false;
            try {
                result = ticketService.ticketingBySpinLock("key", ticketId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (result) {
                System.out.println("티켓 구매에 성공했습니다 !");
                ticketService.addMember(ticketId, memberId);
            }
            else {
                System.out.println("티켓 구매에 실패했습니다 !");
            }

            countDownLatch.countDown();
        }
    }
}