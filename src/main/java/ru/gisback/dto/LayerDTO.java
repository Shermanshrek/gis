package ru.gisback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.gisback.model.Layer;
import ru.gisback.model.Role;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LayerDTO {
    private Long id;
    private String layerName;
    private Role role;
    private List<ObjectGeometryDTO> objects;

    public static LayerDTO toDTO(Layer layer) {
        LayerDTO dto = new LayerDTO();
        dto.setId(layer.getId());
        dto.setLayerName(layer.getLayerName());
        dto.setRole(layer.getRole());

        if (layer.getObjects() != null) {
            dto.setObjects(
                    layer.getObjects().stream()
                            .map(ObjectGeometryDTO::toDTO)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    public Layer toEntity() {
        Layer layer = new Layer();
        layer.setId(this.id);
        layer.setLayerName(this.layerName);
        layer.setRole(this.role);
        return layer;
    }
}