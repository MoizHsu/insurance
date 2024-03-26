package com.jason.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jason.model.entity.UserEntity;
import com.jason.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "user with account %s not found";

    @Autowired
	UserRepository userRepository;

    public String getUserIdByAccount(String account) {
        Optional<UserEntity> user = userRepository.findByAccount(account);
        return user.get().getId();
    }

    public UserEntity getUserByAccount(String account) {
        return userRepository.findByAccount(account).orElseThrow();
    }

    public UserEntity addUser(UserEntity user) {
        return userRepository.save(user);
    }
    
    public UserEntity updateUser(UserEntity user) {
        if (user.getId() == "") {
        }
        return userRepository.saveAndFlush(user);
    }

    public Integer getUserId() {
        System.out.println("-- user service getUserId --");
        int userId=5;
        return userId;
    }

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        return userRepository.findByAccount(account)
            .orElseThrow(
                () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, account))
            );
    }
}
