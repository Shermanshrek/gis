package ru.gisback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.gisback.model.Layer;
import ru.gisback.model.Role;
import ru.gisback.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private List<Long> layerIds;

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());

        if (user.getLayers() != null) {
            dto.setLayerIds(
                    user.getLayers().stream()
                            .map(Layer::getId)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setRole(Role.valueOf(this.role));
        return user;
    }
}
