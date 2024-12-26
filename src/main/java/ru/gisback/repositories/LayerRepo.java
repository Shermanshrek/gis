package ru.gisback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gisback.model.LayerModel;
import ru.gisback.model.Role;

import java.util.List;
import java.util.Optional;

public interface LayerRepo extends JpaRepository<LayerModel, Long> {
    Optional<LayerModel> findByLayerName(String name);
    List<LayerModel> findAllByRole(Role role);
}
