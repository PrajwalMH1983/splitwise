package com.splitwise.money.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"groupsInUser"})
@ToString(exclude = {"groupsInUser"})
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "phoneNumber")
        , @UniqueConstraint(columnNames = "email")}, name = "my_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NonNull
    private String userName;

    @NonNull
    private String password;
    @NonNull
    @Email
    private String email;

    @NonNull
    private String phoneNumber;

    @ManyToMany(
            cascade = CascadeType.ALL ,
            fetch=FetchType.LAZY,
            mappedBy = "usersInGroup"
    )
//    @JoinTable(
//            name = "user_group_map",
//            joinColumns = @JoinColumn(
//                    name = "user_id",
//                    referencedColumnName = "userId"
//            ),
//            inverseJoinColumns = @JoinColumn(
//                    name = "group_id",
//                    referencedColumnName = "groupId"
//            )
//    )
    private Set<Group> groupsInUser = new HashSet<>();

}
