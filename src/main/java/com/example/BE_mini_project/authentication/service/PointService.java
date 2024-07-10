package com.example.BE_mini_project.authentication.service;

import com.example.BE_mini_project.authentication.dto.PaymentResult;
import com.example.BE_mini_project.authentication.model.Point;
import com.example.BE_mini_project.authentication.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {

    private final PointRepository pointRepository;

    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public List<Point> getAvailablePointsSortedByExpiration(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return pointRepository.findByInviterIdAndExpiredAtAfterOrderByExpiredAtAsc(userId, now);
    }

    public PaymentResult usePointsForPayment(Long userId, double amount) {
        List<Point> availablePoints = getAvailablePointsSortedByExpiration(userId);
        double remainingAmount = amount;
        int pointsUsed = 0;
        List<Point> updatedPoints = new ArrayList<>();

        for (Point point : availablePoints) {
            if (remainingAmount <= 0) break;

            int pointsToUse = Math.min(point.getPoints(), (int) Math.ceil(remainingAmount));
            pointsUsed += pointsToUse;
            remainingAmount -= pointsToUse;

            point.setPoints(point.getPoints() - pointsToUse);
            updatedPoints.add(point);
        }

        pointRepository.saveAll(updatedPoints);

        return new PaymentResult(pointsUsed, remainingAmount);
    }
}
