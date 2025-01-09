package redisX.ticket.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redisX.ticket.domain.Ticket;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TicketServiceTest {

    @Autowired
	private TicketService ticketService;
    @Test
    void contextLoads() {
    }

	@Test
	@DisplayName("제한 수량이 2개인 티켓에 이미 1개의 티켓이 나갔고, 남은 1개의 티켓에 대해 10명이 동시에 참여하는 상황")
	void ticketingTest() throws InterruptedException {
		// given
		final Long TOTAL_TICKETS = 10L; // 총 10개의 티켓
		final int TOTAL_USERS = 20; // 20명이 동시에 구입하려고 시도
		final Long TICKET_ID = ticketService.create(TOTAL_TICKETS); // 테스트할 티켓 ID

		CountDownLatch countDownLatch = new CountDownLatch(TOTAL_USERS); // 20명 대기

		// when
		List<TicketPurchaseWorker> workers = Stream.generate(() -> new TicketPurchaseWorker(ticketService, TICKET_ID, countDownLatch))
				.limit(TOTAL_USERS)
				.collect(Collectors.toList());

		workers.forEach(worker -> new Thread(worker).start());
		countDownLatch.await(); // 모든 작업이 완료될 때까지 대기

		// then
		Ticket updateTicket = ticketService.findById(TICKET_ID);
		assertThat(updateTicket.getQuantity()).isEqualTo(0);
	}

	private class TicketPurchaseWorker implements Runnable {
		private final TicketService ticketService;
		private final Long ticketId;
		private final CountDownLatch countDownLatch;

		public TicketPurchaseWorker(TicketService ticketService, Long ticketId, CountDownLatch countDownLatch) {
			this.ticketService = ticketService;
			this.ticketId = ticketId;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			boolean result = ticketService.ticketing(ticketId);
			if (result) {
				System.out.println("티켓 구매에 성공했습니다 !");
			}
			else {
				System.out.println("티켓 구매에 실패했습니다 !");
			}

			countDownLatch.countDown();
		}
	}
}
