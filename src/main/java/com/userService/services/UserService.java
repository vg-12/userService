package com.userService.services;

import com.userService.models.Token;
import com.userService.models.User;

public interface UserService {
     Token login(String email, String password);
     User signUp(String name, String email, String password);
     User validateToken(String tokenValue);
     void logout(String tokenValue);
}
