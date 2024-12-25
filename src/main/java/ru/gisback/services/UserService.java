package ru.gisback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.gisback.dto.UserDTO;
import ru.gisback.model.UserModel;
import ru.gisback.model.Role;
import ru.gisback.repositories.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepository;

    @Autowired
    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(UserDTO user) {
        Optional<UserModel> userOptional = userRepository.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new UsernameNotFoundException("Username already exists");
        }
        UserModel model = new UserModel();
        model.setUsername(user.getUsername());
        model.setPassword(user.getPassword());
        model.setRole(Role.valueOf(user.getRole()));
        userRepository.save(model);
    }

    public UserModel getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public UserModel getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public void setRole(String username, String role) {
        Optional<UserModel> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            UserModel model = user.get();
            model.setRole(Role.valueOf(role));
        } else throw new RuntimeException("Пользователь не найден");
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void save(UserModel user){
        userRepository.save(user);
    }

    //дать права администратора текущему пользователю
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
