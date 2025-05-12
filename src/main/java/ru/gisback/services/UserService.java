package ru.gisback.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gisback.dto.UserDTO;
import ru.gisback.model.Layer;
import ru.gisback.model.User;
import ru.gisback.model.Role;
import ru.gisback.repositories.LayerRepo;
import ru.gisback.repositories.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepository;
    private final LayerRepo layerRepo;

    public UserService(UserRepo userRepository, LayerRepo layerRepo) {
        this.userRepository = userRepository;
        this.layerRepo = layerRepo;
    }

    @Transactional
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with this username already exists");
        }

        if (user.getRole() == null) {
            user.setRole(Role.ROLE_LEVEL1);
        }

        user.setLayers(layerRepo.findAllByRole(user.getRole()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByUsername(username);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        user.setRole(role);
        user.setLayers(layerRepo.findAllByRole(role));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                user.getLayers().stream()
                        .map(Layer::getId)
                        .collect(Collectors.toList())
        );
    }

    public UserDTO getUserDTO(User user) {
        return convertToDTO(user);
    }
}