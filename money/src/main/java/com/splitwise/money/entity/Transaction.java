package com.splitwise.money.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@EqualsAndHashCode
@EntityListeners(TransactionListener.class)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotNull
    private Double amount;
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime paidAt;

    @ManyToOne(
            cascade = CascadeType.ALL ,
            fetch= FetchType.LAZY
    )
    @JoinColumn(name = "payer_user_id")
    private User payer;

    @ManyToOne(
            cascade = CascadeType.ALL ,
            fetch= FetchType.LAZY
    )
    @JoinColumn(name = "receiver_user_id")
    private User receiver;


}
