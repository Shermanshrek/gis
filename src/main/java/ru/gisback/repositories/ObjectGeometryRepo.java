package ru.gisback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gisback.model.geometry.ObjectGeometry;

import java.util.List;

public interface ObjectGeometryRepo extends JpaRepository<ObjectGeometry, Long> {
    List<ObjectGeometry> findByLayerId(Long layerId);
}
