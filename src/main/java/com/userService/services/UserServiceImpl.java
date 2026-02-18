package com.userService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userService.dtos.SendEmailDto;
import com.userService.exceptions.TokenInvalidException;
import com.userService.models.Token;
import com.userService.models.User;
import com.userService.repositories.TokenRepository;
import com.userService.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private ObjectMapper objectMapper;
//    key-> topic, value-> event
    private KafkaTemplate<String,String> kafkaTemplate;
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, TokenRepository tokenRepository, KafkaTemplate kafkaTemplate, ObjectMapper objectMapper){
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.userRepository=userRepository;
        this.tokenRepository=tokenRepository;
        this.kafkaTemplate=kafkaTemplate;
        this.objectMapper=objectMapper;
    }
    @Override
    public Token login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null){
//            throw or redirect to signup page
            return null;
        }
//        match the password
        if (!bCryptPasswordEncoder.matches(password,user.getPassword())){
//            throw mismatch exception
            return null;
        }
//        login success-> generate token
        Token token = new Token();
//        to generate random string there are 2 ways
//        1 use UUID
//        UUID.randomUUID().toString();
//        2 use apache commons lang lib
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        LocalDate localDate=LocalDate.now().plusDays(30);
        Date expiryDate= Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        token.setExpiryAt(expiryDate);

        return tokenRepository.save(token);
    }

    @Override
    public User signUp(String name, String email, String password) throws JsonProcessingException {
        User user=new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));

//        push an event to kafka which userService will read and send welcome email to the user.
        SendEmailDto emailDto= new SendEmailDto();
        emailDto.setBody("Thank you for using our user service");
        emailDto.setSubject("welcome to user service");
        emailDto.setEmail(email);

//        push an event to kafka.
//        objectMapper converts object to json-> since kafka accepts the message in the form of JSON hence we convert the email to json
        kafkaTemplate.send(
                "sendEmail",
                objectMapper.writeValueAsString(emailDto));
        return userRepository.save(user);
    }

    @Override
    public User validateToken(String tokenValue) {
//        token value should be present in the db
//        deleted should be false
//        expiry time > current time
        Optional<Token> optionalToken=tokenRepository.findByValueAndIsDeletedAndExpiryAtGreaterThan(tokenValue,false,new Date());
        return optionalToken.map(Token::getUser).orElse(null);
    }

    @Override
    public void logout(String tokenValue) {
        Token token = tokenRepository.findByValueAndIsDeletedAndExpiryAtGreaterThan(tokenValue, false, new Date())
                .orElseThrow(() -> new TokenInvalidException("Token is invalid or already logged out"));

        token.setDeleted(true);
        tokenRepository.save(token);
    }
}
