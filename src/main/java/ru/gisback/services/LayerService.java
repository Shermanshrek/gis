package ru.gisback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gisback.model.LayerModel;
import ru.gisback.model.Role;
import ru.gisback.model.UserModel;
import ru.gisback.repositories.LayerRepo;
import ru.gisback.repositories.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LayerService {
    private final LayerRepo layerRepo;
    private final UserRepo userRepo;

    @Autowired
    public LayerService(LayerRepo layerRepo, UserRepo userRepo) {
        this.layerRepo = layerRepo;
        this.userRepo = userRepo;
    }

    public void addLayer(String name) {
        Optional<LayerModel> layer = layerRepo.findByLayerName(name);
        if (layer.isPresent()) {
            throw new RuntimeException("Layer already exists");
        }
        LayerModel layerModel = new LayerModel();
        layerModel.setLayerName(name);
        layerRepo.save(layerModel);
    }

    private boolean hasAccess(Role userRole, Role layerRole) {
        return userRole.ordinal() >= layerRole.ordinal();
    }

    public List<LayerModel> getAccessibleLayers(Long id) {
        Optional<UserModel> user = userRepo.findById(id);
        Role userRole;
        if(user.isPresent()){
            userRole = user.get().getRole();
        }
        else throw new RuntimeException("User not found");
        List<LayerModel> allLayers = layerRepo.findAll(); // Получите все слои
        return allLayers.stream()
                .filter(layer -> hasAccess(userRole, layer.getRole())) // Фильтруйте по доступу
                .collect(Collectors.toList());
    }
}
