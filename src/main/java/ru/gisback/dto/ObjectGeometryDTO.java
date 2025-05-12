package ru.gisback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gisback.model.geometry.ObjectGeometry;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectGeometryDTO {
    private Long id;
    private String description;
    private List<Double> points;
    private int dimension;
    private Long layerId;

    public static ObjectGeometryDTO toDTO(ObjectGeometry geometry) {
        ObjectGeometryDTO dto = new ObjectGeometryDTO();
        dto.setId(geometry.getId());
        dto.setDescription(geometry.getDescription());
        dto.setPoints(geometry.getPoints());
        dto.setDimension(geometry.getDimension());
        if (geometry.getLayer() != null) {
            dto.setLayerId(geometry.getLayer().getId());
        }
        return dto;
    }
}
