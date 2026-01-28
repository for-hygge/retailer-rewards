package com.retailer.rewards.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class CreateCustomer {
    @NonNull
    private String name;

    private Integer purchase;

    private Integer rewards;
}
