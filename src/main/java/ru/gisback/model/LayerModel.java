package ru.gisback.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class LayerModel {
    @Id
    private String layerName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
}
