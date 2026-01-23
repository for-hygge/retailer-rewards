package com.retailer.rewards.DTO;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@ToString
public class CreateCustomer {
    @NonNull
    private String name;

    private Integer purchase;

    private Integer rewards;
}
