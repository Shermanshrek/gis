package ru.gisback.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gisback.dto.LayerDTO;
import ru.gisback.dto.ObjectGeometryDTO;
import ru.gisback.model.Layer;
import ru.gisback.model.Role;
import ru.gisback.model.User;
import ru.gisback.repositories.LayerRepo;
import ru.gisback.repositories.ObjectGeometryRepo;
import ru.gisback.repositories.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LayerService {
    private final LayerRepo layerRepo;
    private final UserRepo userRepo;
    private final ObjectGeometryRepo objectGeometryRepo;


    public void addLayer(String name, String role) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Layer name must not be blank");
        }
        Optional<Layer> layer = layerRepo.findByLayerName(name);
        if (layer.isPresent()) {
            throw new IllegalArgumentException("Layer already exists: " + name);
        }
        Layer layerModel = new Layer();
        layerModel.setLayerName(name);
        layerModel.setRole(Role.valueOf(role));
        layerRepo.save(layerModel);
    }

    private boolean hasAccess(Role userRole, Role layerRole) {
        return userRole.ordinal() >= layerRole.ordinal();
    }

    public List<LayerDTO> getAccessibleLayers(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Layer> allLayers = layerRepo.findAll();

        return allLayers.stream()
                .filter(layer -> hasAccess(user.getRole(), layer.getRole()))
                .map(LayerDTO::toDTO)
                .collect(Collectors.toList());
    }

    public List<ObjectGeometryDTO> getLayerObjects(Long layerId) {
        return objectGeometryRepo.findByLayerId(layerId).stream()
                .map(ObjectGeometryDTO::toDTO)
                .collect(Collectors.toList());
    }

    public LayerDTO createLayer(LayerDTO dto){
        // роль определяет, каким пользователям виден слой; если фронт её не прислал —
        // берём самый низкий уровень (виден всем)
        Role role = dto.getRole() != null ? dto.getRole() : Role.ROLE_LEVEL1;
        addLayer(dto.getLayerName(), role.name());
        // возвращаем только что сохранённый слой
        return layerRepo.findByLayerName(dto.getLayerName())
                .map(LayerDTO::toDTO)
                .orElseThrow();
    }

    public List<LayerDTO> getAllLayers() {
        return layerRepo.findAll().stream()
                .map(LayerDTO::toDTO)
                .collect(Collectors.toList());
    }

}
