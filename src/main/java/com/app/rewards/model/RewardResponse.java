package com.app.rewards.model;

import java.util.Map;

public class RewardResponse {
    private String customerId;
    private long totalRewards;

    public RewardResponse(String customerId, long totalRewards) {
        this.customerId = customerId;
        this.totalRewards = totalRewards;
    }

    public String getCustomerId() {
        return customerId;
    }

    public long getTotalRewards() {
        return totalRewards;
    }
}
