package com.example.mysqlpersistencia.democonmysq.security.auth;

import com.example.mysqlpersistencia.democonmysq.security.config.JwtService;
import com.example.mysqlpersistencia.democonmysq.security.user.Role;
import com.example.mysqlpersistencia.democonmysq.security.user.User;
import com.example.mysqlpersistencia.democonmysq.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//autenticación y registro de usuarios
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    //inyecciones

    //repositorio
private final UserRepository repository;
//codificar las contraseñas de los usuarios
private final PasswordEncoder passwordEncoder;
//generar tokens JWT
private final JwtService jwtService;
//autenticar las credenciales de un usuario.
private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        //Check if email exists
        var email = request.getEmail();

        if (repository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
