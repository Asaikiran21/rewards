package com.app.rewards.service;

import com.app.rewards.repository.TransactionRepository;
import com.app.rewards.model.RewardResponse;
import com.app.rewards.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RewardsService {

    @Autowired
    private TransactionRepository transactionRepository;


    /**
     * get Reward points for a customer by customerId
     * @param customerId
     * @return
     */
    public RewardResponse getRewardsSummaryForCustomer(String customerId) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        List<Transaction> transactions = transactionRepository.findTransactionByCustomerIdAndDate(threeMonthsAgo,customerId);
        return aggregatePoints(customerId,transactions);
    }

    /**
     * get Reward points for all the customers
     * @return
     */
    public Map<String, Map<String, Long>> getRewardsSummaryForAllCustomers() {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        List<Transaction> transactions = transactionRepository.findTransactionsByDate(threeMonthsAgo);

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Map<String, Long>> pointsSummary = new HashMap<>();

        for (Transaction transaction : transactions) {
            String customerId = transaction.getCustomerId();
            String month = transaction.getDate().format(monthFormatter);
            long points = calculatePoints(transaction.getAmount());
            pointsSummary
                    .computeIfAbsent(customerId, k -> new HashMap<>())
                    .merge(month, points, Long::sum);
        }
        // Add totals
        pointsSummary.forEach((customer, months) -> {
            long total = months.values().stream().mapToLong(Long::longValue).sum();
            months.put("Total", total);
        });
        return pointsSummary;
    }

    //Helper method for single customer computation
    private RewardResponse aggregatePoints(String customerId,List<Transaction> transactions) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Long> pointsMap = new HashMap<>();

        for (Transaction transaction : transactions) {
            String month = transaction.getDate().format(monthFormatter);
            long points = calculatePoints(transaction.getAmount());
            pointsMap.merge(month, points, Long::sum);
        }

        // Add Total
        long total = pointsMap.values().stream().mapToLong(Long::longValue).sum();
        return new RewardResponse(customerId,total);
    }

    //Logic for rewards calculation
    public long calculatePoints(double amount) {
        if (amount <= 50) {
            return 0;
        } else if (amount <= 100) {
            return Math.round(amount - 50);
        } else {
            return 50 + Math.round((amount - 100) * 2);
        }
    }
}