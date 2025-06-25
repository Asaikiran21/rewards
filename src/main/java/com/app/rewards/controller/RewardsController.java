package com.app.rewards.controller;

import com.app.rewards.service.RewardsService;
import com.app.rewards.model.RewardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rewards")
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;


    @GetMapping("/{customerId}")
    public RewardResponse getRewards(@PathVariable String customerId) {
        return rewardsService.getRewardsSummaryForCustomer(customerId);
    }

    @GetMapping
    public Map<String, Map<String, Long>> getAllRewards() {
        return rewardsService.getRewardsSummaryForAllCustomers();
    }
}
