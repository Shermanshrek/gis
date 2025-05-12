package ru.gisback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gisback.model.Layer;
import ru.gisback.model.Role;
import ru.gisback.model.geometry.ObjectGeometry;

import java.util.List;
import java.util.Optional;

public interface LayerRepo extends JpaRepository<Layer, Long> {
    Optional<Layer> findByLayerName(String name);
    List<Layer> findAllByRole(Role role);
}
