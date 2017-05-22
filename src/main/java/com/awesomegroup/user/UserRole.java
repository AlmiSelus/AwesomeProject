package com.awesomegroup.user;

import javax.persistence.*;

/**
 * Created by Michał on 2017-04-23.
 */
@Entity
@Table(name = "user_roles")
public class UserRole {

    public static final UserRole ADMIN_ROLE = UserRole.create().name("ADMIN").build();

    @Id
    @GeneratedValue
    @Column(name="role_id", nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="role_name", nullable = false, unique = true)
    private String role;

    public long getId() {
        return id;
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

    public static UserRoleBuilder create() {
        return new UserRoleBuilder();
    }

    public static UserRoleBuilder create(UserRole userRole) {
        return new UserRoleBuilder(userRole);
    }

    public static class UserRoleBuilder {
        private String name;
        private User user;

        public UserRoleBuilder() {}

        public UserRoleBuilder(UserRole userRole) {
            name = userRole.role;
            user = userRole.user;
        }

        public UserRoleBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserRoleBuilder user(User user) {
            this.user = user;
            return this;
        }

        public UserRole build() {
            UserRole role = new UserRole();
            role.role = name;
            role.user = user;
            return role;
        }
    }
}
