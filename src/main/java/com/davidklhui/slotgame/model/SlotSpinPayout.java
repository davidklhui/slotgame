package com.davidklhui.slotgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
    Payout class defines the amount of payout is given to the player
    this is different from PayoutDefinition is because:
        1. Payout Definition defines the payout if matched (regular pattern)
        2. Payout Definition defines the payout if matched (wild symbols involved) --- TBC
        3. this payoutAmount can be 0
 */
@AllArgsConstructor
@Getter
public class SlotSpinPayout {

    private final PayoutDefinition payoutDefinition;
    private final int payoutAmount;

}
