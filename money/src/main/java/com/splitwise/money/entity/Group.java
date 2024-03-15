package com.splitwise.money.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;



@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Data
@EqualsAndHashCode(exclude = {"usersInGroup" , "expenses"})
@ToString(exclude = {"usersInGroup" , "expenses"})
@Table(name = "my_group")
public class Group{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    private String description;
    @NonNull
    private String title;

    @ManyToMany(
            cascade = CascadeType.ALL ,
            fetch=FetchType.LAZY
    )
    @JoinTable(
            name = "user_group_map",
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "userId"
            ),
            joinColumns = @JoinColumn(
                    name = "group_id",
                    referencedColumnName = "groupId"
            )
    )
    private Set<User> usersInGroup = new HashSet<>();
    @OneToMany(
            cascade = CascadeType.ALL ,
            fetch=FetchType.LAZY,
            mappedBy = "involvedGroup"
    )
    private Set<Expense> expenses = new HashSet<>();


}
