package ru.gisback.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gisback.dto.CreateObjectGeometryDTO;
import ru.gisback.dto.ObjectGeometryDTO;
import ru.gisback.model.Layer;
import ru.gisback.model.geometry.ObjectGeometry;
import ru.gisback.repositories.LayerRepo;
import ru.gisback.repositories.ObjectGeometryRepo;

@Service
@RequiredArgsConstructor
public class ObjectGeometryService {
    private final ObjectGeometryRepo objectRepo;
    private final LayerRepo layerRepo;

    @Transactional
    public ObjectGeometryDTO addObject(Long layerId, CreateObjectGeometryDTO dto) {
        Layer layer = layerRepo.findById(layerId)
                .orElseThrow(() -> new IllegalArgumentException("Layer not found"));

        ObjectGeometry geom = new ObjectGeometry(
                dto.description(),
                dto.points(),
                layer,
                dto.dimension()
        );

        ObjectGeometry saved = objectRepo.save(geom);
        return ObjectGeometryDTO.toDTO(saved);
    }

    /* — NEW — */
    @Transactional
    public void deleteObject(Long layerId, Long objId) {
        ObjectGeometry geom = objectRepo.findById(objId)
                .orElseThrow(() -> new IllegalArgumentException("Object not found"));

        if (!geom.getLayer().getId().equals(layerId)) {
            throw new IllegalStateException("Object does not belong to this layer");
        }
        objectRepo.deleteById(objId);
    }
}
