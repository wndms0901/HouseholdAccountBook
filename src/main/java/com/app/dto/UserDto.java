package com.app.dto;

import com.app.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String newPassword;
    private String monthStartDate;

    @Builder
    public UserDto(Long id, String name, String email, String password, String newPassword, String monthStartDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.newPassword = newPassword;
        this.monthStartDate = monthStartDate;
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .monthStartDate(monthStartDate).build();
    }
}
