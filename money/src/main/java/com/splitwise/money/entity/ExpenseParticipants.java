package com.splitwise.money.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;


@Data
@Entity
@EqualsAndHashCode()
@ToString()
@Table(name = "expense_participants")
public class ExpenseParticipants {

    @EmbeddedId
    @NotNull
    private CompositeKey id;

    public ExpenseParticipants() {}

    public ExpenseParticipants(CompositeKey compositeKey) {
        if (compositeKey == null || compositeKey.getExpense_id() == null || compositeKey.getEid() == null) {
            throw new IllegalArgumentException("Composite key cannot be null or contain null values");
        }
        this.id = compositeKey;
    }

    @ManyToOne
    @MapsId("expense_id")
    //, nullable = false ,  foreignKey = @ForeignKey(name = "fk_expense_id")
    @JoinColumn(name = "expense_id" , referencedColumnName = "expenseId" ,  nullable = false)
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "paid_by_user_id" , nullable = false)
    private User payer;

    @ManyToOne
    @JoinColumn(name = "participant_user_id" , nullable = false)
    private User participant;

    @Column(name = "amount_owed" )
    @NotNull
    private Double amountOwed;
}
