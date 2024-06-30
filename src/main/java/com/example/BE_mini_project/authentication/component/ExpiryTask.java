package com.example.BE_mini_project.authentication.component;

import com.example.BE_mini_project.authentication.repository.DiscountRepository;
import com.example.BE_mini_project.authentication.repository.PointRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExpiryTask {

    private final DiscountRepository discountRepository;
    private final PointRepository pointRepository;

    public ExpiryTask(DiscountRepository discountRepository, PointRepository pointRepository) {
        this.discountRepository = discountRepository;
        this.pointRepository = pointRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkExpiry() {
        LocalDateTime now = LocalDateTime.now();

        discountRepository.updateExpiredDiscounts(now);

        pointRepository.updateExpiredPoints(now);
    }
}
