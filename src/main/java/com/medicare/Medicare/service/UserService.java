package com.medicare.Medicare.service;

import com.medicare.Medicare.dto.LoginDto;
import com.medicare.Medicare.dto.SignUpDto;
import com.medicare.Medicare.dto.UserDto;
import com.medicare.Medicare.entity.User;
import com.medicare.Medicare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
        if(user.isPresent()){
            throw new RuntimeException("User already exists with email: " + signUpDto.getEmail());
        }
        User toCreate = modelMapper.map(signUpDto, User.class);
        toCreate.setPasswordHash(passwordEncoder.encode(signUpDto.getPassword()));
        User created = userRepository.save(toCreate);
        return modelMapper.map(created, UserDto.class);
    }

}
