package com.davidklhui.slotgame.model;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;

/*
    This declares the payline definition
    in the future this should be in the db
 */
@Entity
@Table(name = "payline")
@Data
public class Payline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payline_id")
    private int paylineId;

    @Column(name = "payline_name")
    private String paylineName;

    @ManyToMany
    @JoinTable(
            name = "payline_coordinates",
            joinColumns = @JoinColumn(name = "payline_id"),
            inverseJoinColumns = @JoinColumn(name = "coordinate_id")
    )
    private List<Coordinate> coordinates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payline payline = (Payline) o;
        if (coordinates == null || payline.coordinates == null) return false;
        if (coordinates.size() != payline.coordinates.size()) return false;
        return CollectionUtils.isEqualCollection(coordinates, payline.coordinates);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(coordinates);
    }
}
