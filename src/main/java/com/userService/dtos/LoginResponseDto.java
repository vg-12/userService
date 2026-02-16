package com.userService.dtos;

import com.userService.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String tokenValue;

    public String getToken() {
        return tokenValue;
    }

    public void setToken(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
