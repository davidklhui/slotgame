package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.SlotException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
    Defines the Slot physical definition
    1. Reels
    2. Number of Rows
 */
@ToString
@Getter
public class Slot {

    private final List<Reel> reels;
    private final int numberOfRows;

    public Slot(){
        this.reels = new ArrayList<>();
        this.numberOfRows = 0;
    }

    @JsonCreator
    public Slot(@JsonProperty("reels") final List<Reel> reels,
                @JsonProperty("numberOfRows") final int numberOfRows){

        if(reels == null){
            throw new SlotException("Incorrect Slot configuration: given reels is null");
        }
        if(reels.size() < 3){
            throw new SlotException(
                    String.format("Incorrect Slot configuration, reels size <3, given %d", reels.size()));
        }
        if(numberOfRows <= 0){
            throw new SlotException(
                    String.format("Incorrect Slot configuration: number of rows is invalid: give %d", numberOfRows));
        }

        this.reels = reels;
        this.numberOfRows = numberOfRows;


    }


    // get the number of columns, aka number of reels
    public int getNumberOfColumns(){
        return getNumberOfReels();
    }

    // get the number of reels
    public int getNumberOfReels(){
        return this.reels.size();
    }

    // find out configured symbols in the slot machine
    public Set<Symbol> distinctSymbols(){
        return this.reels.stream()
                .flatMap(x-> x.getSymbols().stream())
                .map(x-> {
                    x.setProbability(BigDecimal.ZERO);
                    return x;
                })
                .collect(Collectors.toSet());
    }

    // get number of different symbols configured in the slot machine
    public int numberOfDistinctSymbols(){
        return this.distinctSymbols().size();
    }

    /* perform simulations (a spin)
     * for each reel -> perform a simulation of the given size
     * then collect them
     *
     * suppose there are 5 reels with 3 rows
     * this will result in an outer-list have 5 items, with inner-list have 3 items
     */
    public List<List<Symbol>> spin(){

        return this.reels.stream()
                .map(reel-> reel.simulate(numberOfRows))
                .toList();

    }

}
