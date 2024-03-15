package com.splitwise.money.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"involvedGroup"})
@ToString(exclude = {"involvedGroup"})
@EntityListeners(EntityListener.class)
@Table(name = "expense")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long expenseId;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "payer_user_id" , nullable = false)
    private User paidBy;

    @ManyToOne
    @JoinColumn(
            name = "group_id",
            referencedColumnName = "groupId"
    )
    private Group involvedGroup;

    @ManyToMany(
            cascade = CascadeType.ALL ,
            fetch=FetchType.LAZY
    )
    @JoinTable(
            name = "expense_participant",
            joinColumns = {
                    @JoinColumn(name="paid_by_user_id" , nullable = false)
            },
            inverseJoinColumns = {@JoinColumn(name = "participant_user_id" , nullable = false)}
    )
    @NotNull
    private Set<User> participants = new HashSet<>();


}
