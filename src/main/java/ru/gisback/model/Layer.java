package ru.gisback.model;

import jakarta.persistence.*;
import lombok.*;
import ru.gisback.model.geometry.ObjectGeometry;

import java.util.List;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "layer_model")
public class Layer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String layerName;

    @ManyToMany(mappedBy = "layers")
    private List<User> users;

    @OneToMany(mappedBy = "layer")
    private List<ObjectGeometry> objects;

    @Enumerated(EnumType.STRING)
    private Role role;
}
