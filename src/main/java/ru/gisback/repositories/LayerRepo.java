package ru.gisback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gisback.model.Layer;

import java.util.Optional;

public interface LayerRepo extends JpaRepository<Layer, Long> {
    Optional<Layer> findByLayerName(String name);
}
