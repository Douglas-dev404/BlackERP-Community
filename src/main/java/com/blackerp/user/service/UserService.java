package com.blackerp.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackerp.shared.exception.ApiException;
import com.blackerp.user.dto.UserResponse;
import com.blackerp.user.dto.UserUpdateRequest;
import com.blackerp.user.entity.User;
import com.blackerp.user.mapper.UserMapper;
import com.blackerp.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return userMapper.toResponse(getOrThrow(id));
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        var user = getOrThrow(id);
        userMapper.updateFromRequest(request, user);
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deactivate(Long id) {
        var user = getOrThrow(id);
        user.setActive(false);
        userRepository.save(user);
    }

    private User getOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Usuário não encontrado"));
    }
}
