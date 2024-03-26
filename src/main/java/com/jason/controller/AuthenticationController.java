package com.jason.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jason.model.request.LoginRequestDTO;
import com.jason.model.request.RegisterRequestDTO;
import com.jason.model.response.AuthenticationResponseDTO;
import com.jason.model.response.GeneralHttpResponseDTO;
import com.jason.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    @Autowired
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<GeneralHttpResponseDTO> register(
        @RequestBody @Validated RegisterRequestDTO request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(
        @RequestBody @Validated LoginRequestDTO request
    ) {
        return ResponseEntity.ok(service.login(request));
    }
}
