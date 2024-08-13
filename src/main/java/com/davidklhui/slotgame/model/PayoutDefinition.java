package com.davidklhui.slotgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/*
    PayoutDefinition defines
    1. the payoutAmount if matched
    2. the payline definition (in what coordinates)
    3. what symbol orders  is matched
    4. the payout amount
 */
@Getter
public class PayoutDefinition {

    // payout definition id
    private int payoutId;

    // list of coordinates
    private final Payline payline;

    // required sequence of symbols, should align with the order of payline coordinates
    private final List<Symbol> symbols;

    // payout if matched
    private final int payoutAmount;

    // payout if only matched wild symbols
    // tbc

    @JsonCreator
    public PayoutDefinition(@JsonProperty final int payoutId,
                            @JsonProperty final Payline payline,
                            @JsonProperty final List<Symbol> symbols,
                            @JsonProperty final int payoutAmount) {

        this.payoutId = payoutId;
        this.payline = payline;
        this.symbols = symbols;
        this.payoutAmount = payoutAmount;
    }


}
