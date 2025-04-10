package ru.gisback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.gisback.model.Layer;
import ru.gisback.model.Role;
import ru.gisback.model.geometry.ObjectGeometry;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LayerDTO {
    private Long id;
    private String layerName;
    private Role requiredRole;
    private List<Long> objectIds;

    public static LayerDTO toDTO(Layer layer) {
        LayerDTO dto = new LayerDTO();
        dto.setId(layer.getId());
        dto.setLayerName(layer.getLayerName());
        dto.setRequiredRole(layer.getRole());

        if (layer.getObjects() != null) {
            dto.setObjectIds(
                    layer.getObjects().stream()
                            .map(ObjectGeometry::getId)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    public Layer toEntity() {
        Layer layer = new Layer();
        layer.setId(this.id);
        layer.setLayerName(this.layerName);
        layer.setRole(this.requiredRole);
        return layer;
    }
}
