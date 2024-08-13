package com.davidklhui.slotgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SlotSpinResult {

    private List<List<Symbol>> spinOutcomes;
    private List<Payout> payouts;


    public int getSpinPayout(){
        return this.payouts.stream()
                .mapToInt(Payout::getPayoutAmount)
                .sum();
    }
}
