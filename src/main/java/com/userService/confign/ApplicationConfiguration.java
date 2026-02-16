package com.userService.confign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration//at time of intialization spring will scan all the methods
public class ApplicationConfiguration {
    @Bean//it creates the object and store it in application context so that we can use it
     public BCryptPasswordEncoder bCryptPasswordEncoder(){
         return new BCryptPasswordEncoder();
     }
}
