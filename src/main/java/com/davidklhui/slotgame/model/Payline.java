package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.PaylineException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;

/*
    This declares the payline definition
    in the future this should be in the db
 */
@Getter
public class Payline {

    private final int paylineId;
    private final String paylineName;
    private final List<PaylineCoordinate> paylineCoordinates;

    @JsonCreator
    public Payline(@JsonProperty("paylineId") final int paylineId,
                   @JsonProperty("paylineName") final String paylineName,
                   @JsonProperty("paylineCoordinates") final List<PaylineCoordinate> paylineCoordinates){

        /*
            if list size is not the same as distinct size
            means there are duplicated coordinates
            should throw exception
         */
        if(paylineCoordinates.size() != paylineCoordinates.stream().distinct().count()){
            throw new PaylineException("Some payline coordinates are duplicated");
        }

        this.paylineId = paylineId;

        // because the name plays no special meaning
        // no checking, accept even if it is null
        this.paylineName = paylineName;

        this.paylineCoordinates = paylineCoordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payline payline = (Payline) o;
        if (paylineCoordinates == null || payline.paylineCoordinates == null) return false;
        if (paylineCoordinates.size() != payline.paylineCoordinates.size()) return false;
        return CollectionUtils.isEqualCollection(paylineCoordinates, payline.paylineCoordinates);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(paylineCoordinates);
    }
}
