package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.PaylineException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/*
    Coordinate Definition: (reel index, row index)
    both are 0-based

    two coordinates c1, c2 are treated equal
    iff c1.reelIndex == c2.reelIndex and c1.rowIndex == c2.rowIndex
 */

@Entity
@Table(name = "coordinate",
        uniqueConstraints = {
            @UniqueConstraint(name = "UK_Coordinate_reel_row", columnNames = {"reel_index", "row_index"})})
@Data
@NoArgsConstructor
@ToString
public class Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordinate_id")
    @JsonIgnore
    private int coordinateId;

    @Column(name = "reel_index")
    private int reelIndex;

    @Column(name = "row_index")
    private int rowIndex;

    // can also use of(reelIndex, rowIndex) to construct the payline coordinate
    @JsonCreator
    public Coordinate(@JsonProperty("reelIndex") final int reelIndex,
                      @JsonProperty("rowIndex") final int rowIndex) {

        if (reelIndex < 0 || rowIndex < 0) {
            throw new PaylineException(
                    String.format("Incorrect reel index or row index, given coordinate = (%d, %d)", reelIndex, rowIndex));
        }

        this.reelIndex = reelIndex;
        this.rowIndex = rowIndex;

    }

    public static Coordinate of(final int reelIndex,
                                final int rowIndex) {

        return new Coordinate(reelIndex, rowIndex);
    }

    /*
        IDE generated codes for equals and hashCode
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return reelIndex == that.reelIndex && rowIndex == that.rowIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reelIndex, rowIndex);
    }
}
