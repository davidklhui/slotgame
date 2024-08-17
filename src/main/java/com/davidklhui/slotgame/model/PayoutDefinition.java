package com.davidklhui.slotgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

/*
    PayoutDefinition defines
    1. the payoutAmount if matched
    2. the payline definition (in what coordinates)
    3. what symbol orders is matched
    4. the payout amount
 */
@Getter
@JsonDeserialize(using = PayoutDefinitionDeserializer.class)
public class PayoutDefinition {

    // payout definition id
    private final int payoutId;

    // if just passed a payline id should be able to recognise the payline
    private final Payline payline;

    // required sequence of symbols, should align with the order of payline coordinates
    private final List<Symbol> symbols;

    // payout if matched
    private final int payoutAmount;

    // payout if only matched wild symbols
    // tbc

    @JsonCreator
    public PayoutDefinition(@JsonProperty("payoutId") final int payoutId,
                            @JsonProperty("payline")final Payline payline,
                            @JsonProperty("symbols")final List<Symbol> symbols,
                            @JsonProperty("payoutAmount")final int payoutAmount) {

        this.payoutId = payoutId;
        this.payline = payline;
        this.symbols = symbols;
        this.payoutAmount = payoutAmount;
    }

    public PayoutDefinition(final int payoutId,
                            final Payline payline,
                            final Symbol symbol,
                            final int payoutAmount) {

        this(payoutId,
                payline,
                Collections.nCopies(payline.getPaylineCoordinates().size(), symbol),
                payoutAmount);

    }



}
