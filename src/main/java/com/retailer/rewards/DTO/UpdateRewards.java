package com.retailer.rewards.DTO;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString
public class UpdateRewards {
    @NonNull
    private Integer transactionMonth;

    @NonNull
    private Integer purchase;
}
