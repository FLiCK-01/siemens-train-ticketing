package com.siemens.train_ticketing.bonus;

import com.siemens.train_ticketing.model.Train;
import com.siemens.train_ticketing.repository.TrainRepository;
import com.siemens.train_ticketing.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/bonus")
@RequiredArgsConstructor
public class ConcurrencyTestController {
    private final BookingService bookingService;
    private final TrainRepository trainRepository;

    @PostMapping("/stress-test")
    public ResponseEntity<String> runStressTest() throws InterruptedException {
        String testTrainId = "TEST-1";
        if(!trainRepository.existsById(testTrainId)) {
            trainRepository.save(new Train(testTrainId, "Test Express", 5));
        }

        int numberOfConcurrentUsers = 100;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfConcurrentUsers);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(numberOfConcurrentUsers);

        AtomicInteger successfulBookings = new AtomicInteger(0);
        AtomicInteger failedBookings = new AtomicInteger(0);

        for(int i = 0; i < numberOfConcurrentUsers; i++) {
            String userEmail = "user" + i + "@test.com";
            executor.submit(() -> {
                try {
                    latch.await();
                    bookingService.bookTicket(userEmail, testTrainId, "CJ", "BV", 1);
                    successfulBookings.incrementAndGet();
                } catch (IllegalStateException | IllegalArgumentException e) {
                    failedBookings.incrementAndGet();
                } catch (Exception e) {
                    failedBookings.incrementAndGet();
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        latch.countDown();

        completionLatch.await();
        executor.shutdown();

        String result = String.format(
                "STRESS TEST ENDED\n" +
                        "Train Capacity: 5 seats\n" +
                        "Concurrent users: %d\n" +
                        "Successful bookings: %d\n" +
                        "Rejected bookings: %d\n" +
                        "Method used: Pessimistic database locking.",
                numberOfConcurrentUsers, successfulBookings.get(), failedBookings.get()
        );

        return ResponseEntity.ok(result);
    }
}
