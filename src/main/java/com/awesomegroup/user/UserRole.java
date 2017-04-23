package com.awesomegroup.user;

import javax.persistence.*;

/**
 * Created by Michał on 2017-04-23.
 */
@Entity
@Table(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue
    @Column(name="role_id", nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="role_name", nullable = false, unique = true)
    private String role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
