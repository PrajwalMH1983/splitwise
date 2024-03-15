package com.splitwise.money.payload.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreateExpenseRequest {
    private Double totalAmount;
    @Size(max=200)
    private String description;

    private String participants;
}
