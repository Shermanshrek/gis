package ru.gisback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.gisback.dto.LayerDTO;
import ru.gisback.dto.UserDTO;
import ru.gisback.model.User;
import ru.gisback.model.Role;
import ru.gisback.repositories.LayerRepo;
import ru.gisback.repositories.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepo userRepository;
    private final LayerRepo layerRepo;

    @Autowired
    public UserService(UserRepo userRepository, LayerRepo layerRepo) {
        this.userRepository = userRepository;
        this.layerRepo = layerRepo;
    }

//    public void addUser(UserDTO user) {
//        Optional<UserModel> userOptional = userRepository.findByUsername(user.getUsername());
//        if (userOptional.isPresent()) {
//            throw new UsernameNotFoundException("Username already exists");
//        }
//        UserModel model = new UserModel();
//        model.setUsername(user.getUsername());
//        model.setPassword(user.getPassword());
//        model.setRole(Role.valueOf(user.getRole()));
//        userRepository.save(model);
//    }

    public User create(User user){
        if (userRepository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Такой пользователь уже существует");
        }
        user.setLayers(layerRepo.findAllByRole(user.getRole()));
        return save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDTO::toDTO)
                .collect(Collectors.toList());
    }

    public void setRole(String username, String role) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User model = user.get();
            model.setRole(Role.valueOf(role));
        } else throw new RuntimeException("Пользователь не найден");
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    //дать права администратора текущему пользователю
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
