package redisX.ticket.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
	@DisplayName("티켓이 10개가 있고, 20명이 티켓팅 하려는 상황")
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


	@Test
	@DisplayName("낙관적 락 사용하여 테스트")
	void ticketingTestUsingOptimisticLock() throws InterruptedException {
		// given
		final Long TOTAL_TICKETS = 10L; // 총 10개의 티켓
		final int TOTAL_USERS = 20; // 20명이 동시에 구입하려고 시도
		final Long TICKET_ID = ticketService.create(TOTAL_TICKETS); // 테스트할 티켓 ID

		CountDownLatch countDownLatch = new CountDownLatch(TOTAL_USERS); // 20명 대기

		// when
		List<TicketPurchaseWorkerV2> workers = Stream.generate(() -> new TicketPurchaseWorkerV2(ticketService, TICKET_ID, countDownLatch))
				.limit(TOTAL_USERS)
				.collect(Collectors.toList());

		workers.forEach(worker -> new Thread(worker).start());
		countDownLatch.await(); // 모든 작업이 완료될 때까지 대기

		// then
		Ticket updateTicket = ticketService.findById(TICKET_ID);
		assertThat(updateTicket.getQuantity()).isEqualTo(0);
	}

	private class TicketPurchaseWorkerV2 implements Runnable {
		private final TicketService ticketService;
		private final Long ticketId;
		private final CountDownLatch countDownLatch;

		public TicketPurchaseWorkerV2(TicketService ticketService, Long ticketId, CountDownLatch countDownLatch) {
			this.ticketService = ticketService;
			this.ticketId = ticketId;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				boolean result = ticketService.ticketingByOptimisticLock(ticketId);
				if (result) {
					System.out.println("구매 성공!");
				} else {
					System.out.println("구매 실패!");
				}
			} catch (ObjectOptimisticLockingFailureException e) {
				System.out.println("구매 실패!");
			} finally {
				countDownLatch.countDown();
			}
		}
	}
}
