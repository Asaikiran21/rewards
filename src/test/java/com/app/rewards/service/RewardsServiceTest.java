package com.app.rewards.service;

import com.app.rewards.model.RewardResponse;
import com.app.rewards.entity.Transaction;
import com.app.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RewardsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardsService rewardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculatePoints_Below50() {
        long result = rewardsService.calculatePoints(45.0);
        assertThat(result).isEqualTo(0);
    }

    @Test
    void testCalculatePoints_Exactly50() {
        long result = rewardsService.calculatePoints(50.0);
        assertThat(result).isEqualTo(0);
    }

    @Test
    void testCalculatePoints_Between51and100() {
        long result = rewardsService.calculatePoints(75.0);
        assertThat(result).isEqualTo(25);
    }

    @Test
    void testCalculatePoints_Above100() {
        long result = rewardsService.calculatePoints(120.0);
        assertThat(result).isEqualTo(90); // 50 for 50-100, (120-100)*2 = 40 → 50+40=90
    }

    @Test
    void testGetRewardsSummaryForCustomer() {
        String customerId = "cust123";
        LocalDate date = LocalDate.now().minusMonths(1);
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1l,customerId, 120.0,date),
                new Transaction(2l,customerId, 80.0,date.minusMonths(1))
        );

        when(transactionRepository.findTransactionByCustomerIdAndDate(any(), eq(customerId)))
                .thenReturn(transactions);

        RewardResponse response = rewardsService.getRewardsSummaryForCustomer(customerId);

        assertThat(response.getCustomerId()).isEqualTo(customerId);
        assertThat(response.getTotalRewards()).isEqualTo(90 + 30); // 120 → 90, 80 → 30
    }

    @Test
    void testGetRewardsSummaryForAllCustomers() {
        LocalDate now = LocalDate.now();
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1l,"cust1",120.0, now.minusMonths(1)),
                new Transaction(2l,"cust1",70.0, now.minusMonths(2)),
                new Transaction(3l,"cust2", 70.0, now.minusMonths(1))
        );

        when(transactionRepository.findTransactionsByDate(any())).thenReturn(transactions);

       List<RewardResponse> summary = rewardsService.getRewardsSummaryForAllCustomers();

        assertThat(summary.stream().map(rewardResponse -> rewardResponse.getCustomerId()).collect(Collectors.toList())).contains("cust1", "cust2");
        assertThat(summary.get(0).getTotalRewards()).isEqualTo(110L);
        assertThat(summary.get(1).getTotalRewards()).isEqualTo(20L);
    }
}
