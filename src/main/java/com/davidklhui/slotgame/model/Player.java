package com.davidklhui.slotgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
    TBC
 */
@AllArgsConstructor
@Getter
public class Player {

    /*
        the player's initial credits when the player enter the game
     */
    private int initialCredits;

    /*
        the trace of the player game spins
     */
    private List<SlotSpinResult> slotSpinResults;

    /*
        Maximum game the player intended to play
     */
    private int maxPlay;

    public List<Integer> creditTrace(int cost){
        final List<Integer> trace = new ArrayList<>();
        trace.add(initialCredits);
        slotSpinResults.forEach(result-> trace.add(initialCredits + result.getSpinPayout() - cost));

        return trace;
    }

    public int getCurrentCredits(int cost){
        return creditTrace(cost).getLast();
    }

    public boolean isRuined(int cost){
        return getCurrentCredits(cost) < cost;
    }


}
