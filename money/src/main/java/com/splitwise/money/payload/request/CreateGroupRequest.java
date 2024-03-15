package com.splitwise.money.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateGroupRequest {

    @Size(max=200)
    private String description;
    @NotBlank
    @Size(max = 70)
    private String title;
}
