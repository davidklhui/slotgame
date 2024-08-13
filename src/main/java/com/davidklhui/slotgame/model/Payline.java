package com.davidklhui.slotgame.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/*
    This declares the payline definition
    in the future this should be in the db
 */
@Getter
public class Payline {

    private final int paylineId;
    private final List<PaylineCoordinate> paylineCoordinates;

    @JsonCreator
    public Payline(@JsonProperty("paylineId") final int paylineId,
                   @JsonProperty("paylineCoordinates") final List<PaylineCoordinate> paylineCoordinates){

        this.paylineId = paylineId;
        this.paylineCoordinates = paylineCoordinates;
    }
}
