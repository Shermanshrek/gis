package ru.gisback.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gisback.dto.LayerDTO;
import ru.gisback.dto.ObjectGeometryDTO;
import ru.gisback.model.Layer;
import ru.gisback.model.Role;
import ru.gisback.model.User;
import ru.gisback.repositories.LayerRepo;
import ru.gisback.repositories.ObjectGeometryRepo;
import ru.gisback.repositories.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LayerService {
    private final LayerRepo layerRepo;
    private final UserRepo userRepo;
    private final ObjectGeometryRepo objectGeometryRepo;

    public LayerDTO createLayer(LayerDTO dto) {
        String name = dto.getLayerName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Layer name must not be blank");
        }
        if (layerRepo.findByLayerName(name).isPresent()) {
            throw new IllegalArgumentException("Layer already exists: " + name);
        }
        Layer layer = new Layer();
        layer.setLayerName(name);
        return LayerDTO.toDTO(layerRepo.save(layer));
    }

    public List<LayerDTO> getAllLayers() {
        return layerRepo.findAll().stream()
                .map(LayerDTO::toDTO)
                .collect(Collectors.toList());
    }

    /** слои, доступные пользователю: админу — все, остальным — только выданные */
    @Transactional(readOnly = true)
    public List<LayerDTO> getAccessibleLayers(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<Layer> layers = user.getRole() == Role.ROLE_ADMIN
                ? layerRepo.findAll()
                : (user.getLayers() != null ? user.getLayers() : List.of());

        return layers.stream()
                .map(LayerDTO::toDTO)
                .collect(Collectors.toList());
    }

    public List<ObjectGeometryDTO> getLayerObjects(Long layerId) {
        return objectGeometryRepo.findByLayerId(layerId).stream()
                .map(ObjectGeometryDTO::toDTO)
                .collect(Collectors.toList());
    }
}
