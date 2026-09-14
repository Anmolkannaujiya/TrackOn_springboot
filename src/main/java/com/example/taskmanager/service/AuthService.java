package com.example.taskmanager.service;

import com.example.taskmanager.dto.AuthResponseDTO;
import com.example.taskmanager.dto.LoginRequestDTO;
import com.example.taskmanager.dto.RegisterRequestDTO;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    //constructor, dependency injection
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    //function for user register
    public void register(RegisterRequestDTO request){

        String email = request.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setUsername(request.getUsername().trim());
        user.setEmail(email);

        //saved hashed password, directly injected into user entity instead of passing through dto
        user.setPassword( passwordEncoder.encode(request.getPassword()));

        user.setRole(Role.USER);

        userRepository.save(user);
    }

    //proper login implementation
    public AuthResponseDTO login(LoginRequestDTO request){

        //clean email
        String email = request.getEmail().trim().toLowerCase();

        //authentication object is being created, this doesn't generate token
        Authentication authentication =  authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                email,
                                                request.getPassword()
                                            )
                                        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //token is generated
        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(
                token,
                "Bearer",
                jwtService.getExpirationSeconds()
        );
    }


    //old and rushed implementation
//    public String login(LoginRequestDTO request){
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(()->new RuntimeException("Invalid email or password"));
//
//        if(!passwordEncoder.matches(
//                request.getPassword(),
//                user.getPassword())){
//
//            throw new RuntimeException("Invalid email or password");
//        }
//        return jwtService.generateToken(user.getEmail());
//    }
}
