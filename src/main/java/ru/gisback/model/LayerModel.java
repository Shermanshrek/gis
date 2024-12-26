package ru.gisback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.ToStringExclude;
import ru.gisback.model.geometry.ObjectGeometry;

import java.util.List;

@Entity
@Data
public class LayerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String layerName;

    @ManyToMany(mappedBy = "layers", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<UserModel> users;

    @OneToMany(mappedBy = "layer", cascade = CascadeType.ALL)
    @ToStringExclude
    @EqualsExclude
    private List<ObjectGeometry> objects;

    @Enumerated(EnumType.STRING)
    private Role role;
}
