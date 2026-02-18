package com.userService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.userService.models.Token;
import com.userService.models.User;

public interface UserService {
     Token login(String email, String password);
     User signUp(String name, String email, String password) throws JsonProcessingException;
     User validateToken(String tokenValue);
     void logout(String tokenValue);
}
