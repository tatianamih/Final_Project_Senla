package com.courses.senla.models;

import com.courses.senla.enums.ActivityStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, exclude = {"discounts", "userAccount", "histories", "roles"})
@Entity
@Table(name = "user_profile")
public class User extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ActivityStatus status;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    @Email(regexp = ".*@.*\\..*", message = "Email should be valid")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "user")
    private List<Discount> discounts;

    @Transient
    private List<String> roleList = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_account_id")
    @ToString.Exclude
    private UserAccount userAccount;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_profile_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "user")
    @ToString.Exclude
    private List<AbonementCardHistory> histories;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "user")
    @ToString.Exclude
    private List<Booking> bookings;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public List<String> getRoleList() {
        for (Role role : roles) {
            roleList.add(role.getName().toString());
        }
        return roleList;
    }
}
