package ru.gisback.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "user_layers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "layer_id")
    )
    private List<Layer> layers;

    // User.java
    @OneToOne(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private RefreshToken refreshToken;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
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
