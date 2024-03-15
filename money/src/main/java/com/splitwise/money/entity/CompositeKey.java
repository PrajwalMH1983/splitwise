package com.splitwise.money.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode()
@ToString()
public class CompositeKey implements Serializable {
//    @ManyToOne
//    @JoinColumns({@JoinColumn(name="paid_by")})
//    private Expense expense;
//
//    @ManyToOne
//    @JoinColumns({ @JoinColumn(name = "participant_user_id")})
//    private User participant;


    @NotNull
    @Column(name = "id")
    private Long eid;


    @NotNull
    @Column(name = "expense_id")
    private Long expense_id;
}
