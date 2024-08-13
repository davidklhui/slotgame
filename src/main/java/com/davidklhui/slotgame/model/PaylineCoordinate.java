package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.SlotException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PaylineCoordinate {

    private final int reelIndex;
    private final int rowIndex;

    // can also use of(reelIndex, rowIndex) to construct the payline coordinate
    @JsonCreator
    public PaylineCoordinate(@JsonProperty("reelIndex") final int reelIndex,
                             @JsonProperty("rowIndex") final int rowIndex) {

        if (reelIndex < 0 || rowIndex < 0) {
            throw new SlotException(
                    String.format("Incorrect reel index or row index, given coordinate = (%d, %d)", reelIndex, rowIndex));
        }

        this.reelIndex = reelIndex;
        this.rowIndex = rowIndex;

    }

    public static PaylineCoordinate of(final int reelIndex,
                                       final int rowIndex) {

        return new PaylineCoordinate(reelIndex, rowIndex);
    }
}
