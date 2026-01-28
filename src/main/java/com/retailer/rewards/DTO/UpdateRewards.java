package com.retailer.rewards.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UpdateRewards {
    @NonNull
    private Integer transactionMonth;

    @NonNull
    private Integer purchase;
}
