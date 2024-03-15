package com.splitwise.money.payload.request;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

    @Size(max = 30)
    @NotBlank
    private String username;

    @NotBlank
    private String phoneNumber;

    @Size(max = 80)
    @NotBlank
    @Email
    private String email;

    @Size(max = 100)
    @NotBlank
    private String password;
}
