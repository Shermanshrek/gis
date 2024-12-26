package ru.gisback.model.geometry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gisback.model.LayerModel;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectGeometry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @ElementCollection
    private List<Double> points;

    @ManyToOne
    @JoinColumn(name = "layer_id")
    @JsonIgnoreProperties("objects")
    private LayerModel layer;

    public ObjectGeometry(String description, List<Double> points, LayerModel layer, int dimension) {
        this.description = description;
        this.points = points;
        this.layer = layer;
        this.dimension = dimension;
    }

    private int dimension;
}
