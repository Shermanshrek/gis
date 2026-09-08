package ru.gisback.model.geometry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.gisback.model.Layer;

import java.util.List;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectGeometry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** локализованное описание: {"ru": "...", "en": "..."} */
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> description;

    @ElementCollection
    private List<Double> points;

    @ManyToOne
    @JoinColumn(name = "layer_id")
    @JsonIgnoreProperties("objects")
    private Layer layer;

    private int dimension;

    public ObjectGeometry(Map<String, String> description, List<Double> points, Layer layer, int dimension) {
        this.description = description;
        this.points = points;
        this.layer = layer;
        this.dimension = dimension;
    }
}
