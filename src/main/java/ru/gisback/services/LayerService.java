package ru.gisback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gisback.dto.LayerDTO;
import ru.gisback.model.Layer;
import ru.gisback.model.Role;
import ru.gisback.model.User;
import ru.gisback.repositories.LayerRepo;
import ru.gisback.repositories.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LayerService {
    private final LayerRepo layerRepo;
    private final UserRepo userRepo;

    public LayerService(LayerRepo layerRepo, UserRepo userRepo) {
        this.layerRepo = layerRepo;
        this.userRepo = userRepo;
    }

    public void addLayer(String name, String role) {
        Optional<Layer> layer = layerRepo.findByLayerName(name);
        if (layer.isPresent()) {
            throw new RuntimeException("Layer already exists");
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
}
