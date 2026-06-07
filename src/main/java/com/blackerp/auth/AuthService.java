package com.blackerp.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blackerp.shared.exception.ApiException;
import com.blackerp.shared.security.JwtService;
import com.blackerp.shared.security.UserPrincipal;
import com.blackerp.user.entity.Role;
import com.blackerp.user.entity.User;
import com.blackerp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw ApiException.conflict("E-mail já cadastrado");
        }

        var user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        var principal = new UserPrincipal(user);
        return AuthResponse.of(
                jwtService.generateToken(principal),
                jwtService.generateRefreshToken(principal)
        );
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> ApiException.notFound("Usuário não encontrado"));

        var principal = new UserPrincipal(user);
        return AuthResponse.of(
                jwtService.generateToken(principal),
                jwtService.generateRefreshToken(principal)
        );
    }
}
