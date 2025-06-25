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
    public List<RewardResponse> getRewardsSummaryForAllCustomers() {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        List<Transaction> transactions = transactionRepository.findTransactionsByDate(threeMonthsAgo);

        Map<String, Long> totalPointsPerCustomer = new HashMap<>();

        for (Transaction transaction : transactions) {
            String customerId = transaction.getCustomerId();
            long points = calculatePoints(transaction.getAmount());
            totalPointsPerCustomer.merge(customerId, points, Long::sum);
        }

        List<RewardResponse> responseList = new ArrayList<>();
        for (Map.Entry<String, Long> entry : totalPointsPerCustomer.entrySet()) {
            responseList.add(new RewardResponse(entry.getKey(), entry.getValue()));
        }

        return responseList;
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