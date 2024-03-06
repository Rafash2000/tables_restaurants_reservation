package com.app.tables_reservations.service;

import com.app.tables_reservations.controller.dto.AuthenticationDto;
import com.app.tables_reservations.model.User;
import com.app.tables_reservations.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> getAllUsers() { return userRepository.findAll(); }

    public User getUserById(Long id) { return userRepository.findById(id).orElse(null); }

    public User addUser(User user) { return userRepository.save(user); }

    public boolean existUser(Long id) { return userRepository.existsById(id); }

    public void deleteUser(Long id) { userRepository.deleteById(id); }

    public User findByEmail(String email) { return userRepository.findByEmail(email).orElse(null); }

    public User save(User user) { return userRepository.save(user); }

    public boolean existUserByEmail(String email) { return userRepository.existsUserByEmail(email); }

    public boolean login(AuthenticationDto authenticationDto) {
        var userByEmail = userRepository.findByEmail(authenticationDto.email()).orElse(null);

        if (userByEmail == null || !userByEmail.isEnabled()) {
            return false;
        }

        var rawPassword = authenticationDto.password();
        return bCryptPasswordEncoder.matches(rawPassword, userByEmail.getPassword());
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
