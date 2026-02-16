package com.userService.controllers;

import com.userService.dtos.*;
import com.userService.models.Token;
import com.userService.models.User;
import com.userService.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController//used for building the rest API's
@RequestMapping("/users")//localhost:8080/users/
public class UserController {

    private UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto){
        Token token=userService.login(requestDto.getEmail(),requestDto.getPassword());
        LoginResponseDto responseDto=new LoginResponseDto();
        responseDto.setToken(token.getValue());
       return responseDto;
    }
    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignUpRequestDto requestDto){
       User user=userService.signUp(requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
       //convert user to userDto
       return UserDto.from(user);
    }
    @PutMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto){
        userService.logout(logoutRequestDto.getTokenValue());
      return null;
    }
//    localhoast:8080/users/validate/token
    @GetMapping("/validate/{token}")
    public ResponseEntity<UserDto> validateToken(@PathVariable("token") String tokenValue){
        User user= userService.validateToken(tokenValue);
        ResponseEntity<UserDto> responseEntity=null;
        if (user==null){
            responseEntity=new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            responseEntity=new ResponseEntity<>(UserDto.from(user),HttpStatus.OK);
        }
        return responseEntity;
    }
}
