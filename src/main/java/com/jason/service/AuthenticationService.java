package com.jason.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jason.model.Role;
import com.jason.model.entity.UserEntity;
import com.jason.model.request.LoginRequestDTO;
import com.jason.model.request.RegisterRequestDTO;
import com.jason.model.response.AuthenticationResponseDTO;
import com.jason.model.response.GeneralHttpResponseDTO;
import com.jason.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class AuthenticationService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtService jwtService;

    public GeneralHttpResponseDTO register (RegisterRequestDTO request) {
        var user = UserEntity.builder()
                .name(request.getName())
                .account(request.getAccount())
                .password(passwordEncoder.encode(request.getPassword()))
                .identification(request.getIdentification())
                .role(Role.USER)
                .build();
        this.userRepository.save(user);
        return GeneralHttpResponseDTO.builder()
                .status("success")
                .build();
    }

    public AuthenticationResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getAccount(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByAccount(request.getAccount()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        log.info(jwtToken);
        return AuthenticationResponseDTO.builder()
                .status("success")
                .token(jwtToken)
                .build();
    }
}
