package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.PaylineException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Objects;

/*
    Coordinate Definition: (reel index, row index)
    both are 0-based

    two coordinates c1, c2 are treated equal
    iff c1.reelIndex == c2.reelIndex and c1.rowIndex == c2.rowIndex
 */
@Getter
public class PaylineCoordinate {

    private final int reelIndex;
    private final int rowIndex;

    // can also use of(reelIndex, rowIndex) to construct the payline coordinate
    @JsonCreator
    public PaylineCoordinate(@JsonProperty("reelIndex") final int reelIndex,
                             @JsonProperty("rowIndex") final int rowIndex) {

        if (reelIndex < 0 || rowIndex < 0) {
            throw new PaylineException(
                    String.format("Incorrect reel index or row index, given coordinate = (%d, %d)", reelIndex, rowIndex));
        }

        this.reelIndex = reelIndex;
        this.rowIndex = rowIndex;

    }

    public static PaylineCoordinate of(final int reelIndex,
                                       final int rowIndex) {

        return new PaylineCoordinate(reelIndex, rowIndex);
    }

    /*
        IDE generated codes for equals and hashCode
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaylineCoordinate that = (PaylineCoordinate) o;
        return reelIndex == that.reelIndex && rowIndex == that.rowIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reelIndex, rowIndex);
    }
}
