package com.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RoleName roleName;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    public void setUser(User user){
        this.user = user;
        user.getRoles().add(this);
    }

    @Builder
    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    public Role update(RoleName roleName) {
        this.roleName = roleName;
        return this;
    }
}
