package ru.gisback.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gisback.model.UserModel;
import ru.gisback.services.AuthenticationService;
import ru.gisback.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/admin/set-role/{username}")
    public ResponseEntity setRole(@PathVariable String username, @RequestBody String role) {
        try {
            userService.setRole(username, role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/admin/delete-user/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
