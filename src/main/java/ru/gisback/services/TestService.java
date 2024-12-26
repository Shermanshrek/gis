package ru.gisback.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gisback.model.LayerModel;
import ru.gisback.model.Role;
import ru.gisback.model.UserModel;
import ru.gisback.model.geometry.ObjectGeometry;
import ru.gisback.repositories.LayerRepo;
import ru.gisback.repositories.ObjectGeometryRepo;
import ru.gisback.repositories.UserRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestService {
    private final ObjectGeometryRepo objectGeometryRepo;
    private final LayerRepo layerRepo;
    private final UserRepo userRepo;


    @Autowired
    public TestService(ObjectGeometryRepo objectGeometryRepo, LayerRepo layerRepo, UserRepo userRepo){
        this.objectGeometryRepo = objectGeometryRepo;
        this.layerRepo = layerRepo;
        this.userRepo = userRepo;
    }
    public void test(){
        LayerModel layer = new LayerModel();
        LayerModel layer2 = new LayerModel();
        layer.setLayerName("test");
        layer2.setLayerName("test2");
        layer2.setRole(Role.ROLE_LEVEL2);
        layer.setRole(Role.ROLE_LEVEL1);
        layerRepo.save(layer);
        layerRepo.save(layer2);
        ObjectGeometry point1 = new ObjectGeometry("point1", List.of(1.0, 2.0), layer, 0);
        ObjectGeometry point2 = new ObjectGeometry("point2", List.of(3.0, 4.0), layer, 0);
        ObjectGeometry point3 = new ObjectGeometry("point3", List.of(5.0, 6.0), layer, 0);
        objectGeometryRepo.save(point1);
        objectGeometryRepo.save(point2);
        objectGeometryRepo.save(point3);
    }
    public List<LayerModel> getLayers(){
       return layerRepo.findAll();
    }

    public boolean hasAccess(Role userRole, Role layerRole) {
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
