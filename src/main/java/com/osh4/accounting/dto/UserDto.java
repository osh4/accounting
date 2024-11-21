package com.osh4.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto implements UserDetails {
    private String id;
    @NotBlank
    private String email;
    private String name;
    @NotBlank
    private String password;
    private Boolean enabled;
    private Set<GrantedAuthority> roles;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isNull(enabled) || enabled;
    }
}
