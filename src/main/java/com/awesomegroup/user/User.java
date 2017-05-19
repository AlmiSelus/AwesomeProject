package com.awesomegroup.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michał on 2017-04-23.
 */
@Entity
@Table(name = "users")
@JsonSerialize
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true)
    private long id;

    @Column(name = "user_name", length = 50)
    private String name;

    @Column(name = "user_surname", length = 50)
    private String surname;

    @Column(name = "user_mail", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "user_password", nullable = false, length = 100)
    private String password;

    @Column(name = "user_enabled", nullable = false)
    private boolean enabled;

    @Column(name = "user_credentials_expired", nullable = false)
    private boolean credentialsExpired;

    @Column(name = "user_locked", nullable = false)
    private boolean locked;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public static Builder create(User user) {
        return new Builder(user);
    }

    public static Builder create() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", credentialsExpired=" + credentialsExpired +
                ", locked=" + locked +
                ", userRoles=" + userRoles +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public static class Builder {
        private User user;

        private Builder() {
            user = new User();
        }

        private Builder(User u) {
            user = u;
        }

        public Builder email(String email) {
            user.email = email;
            return this;
        }

        public Builder password(String password) {
            user.password = password;
            return this;
        }

        public Builder locked(boolean isLocked) {
            user.locked = isLocked;
            return this;
        }

        public Builder enabled(boolean enabled) {
            user.enabled = enabled;
            return this;
        }

        public Builder credentialsExpired(boolean isCredentialsExpired) {
            user.credentialsExpired = isCredentialsExpired;
            return this;
        }

        public Builder roles(UserRole... roles) {
            Collections.addAll(user.getUserRoles(), roles);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
