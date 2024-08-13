package com.davidklhui.slotgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class SlotGameDefinition {

    private final Slot slot;
    private final List<PayoutDefinition> payoutDefinitions;

    private final int cost;

    @JsonCreator
    public SlotGameDefinition(@JsonProperty("slot") final Slot slot,
                              @JsonProperty("payoutDefinitions") final List<PayoutDefinition> payoutDefinitions,
                              @JsonProperty("cost") final int cost){

        this.slot = slot;
        this.payoutDefinitions = payoutDefinitions;
        this.cost = cost;
    }
}
