package com.jason.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jason.model.entity.UserEntity;
import com.jason.model.response.user.GetUserInfoResponseDTO;
import com.jason.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
	UserService userService;

    @RequestMapping("/ex")
    public String edx(){
		return "index";
    }

    @GetMapping("/info")
    public ResponseEntity<GetUserInfoResponseDTO> getUserInfo(Authentication authentication) {
        UserEntity user = userService.getUserByAccount(authentication.getName());
        var userInfo = GetUserInfoResponseDTO.builder()
        .id(user.getId())
        .name(user.getName())
        .account(user.getAccount())
        .identification(user.getIdentification())
        .telephone(user.getTelephone())
        .role(user.getRole()).build();
        return ResponseEntity.ok(userInfo);
    }

    @RequestMapping("new")
    public String newUser(){
    	var user = new UserEntity();
        user.setAccount("test@cathayholdings.com.tw");
        user.setName("cahtayholdings");
        user.setPassword("testPassword");
        user.setTelephone("0900123456");
    	userService.addUser(user);
        return "新增會員成功";
    }
}
