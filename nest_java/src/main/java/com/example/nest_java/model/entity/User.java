package com.example.nest_java.model.entity;

import com.example.nest_java.model.BaseEntity;
import com.example.nest_java.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "account")
public class User extends BaseEntity implements UserDetails {
    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 75)
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "dob")
    private Date dateOfBirth;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    private String avatar;

    @Enumerated(EnumType.STRING)    // giá trị của enum sẽ được lưu dưới dạng string trong db
    @Column(length = 16)
    private Role role;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }



}
