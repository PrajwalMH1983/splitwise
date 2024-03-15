package com.splitwise.money.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TransactionRequest {
    @NotNull
    private Double amount;
    private String description;

}
