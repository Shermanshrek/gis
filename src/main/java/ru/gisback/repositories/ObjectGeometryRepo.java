package ru.gisback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gisback.model.geometry.ObjectGeometry;

public interface ObjectGeometryRepo extends JpaRepository<ObjectGeometry, Long> {
}
