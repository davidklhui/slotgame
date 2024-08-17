package com.davidklhui.slotgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SlotSpinResult {

    private List<List<Symbol>> spinOutcomes;
    private List<SlotSpinPayout> slotSpinPayouts;


    public int getSpinPayout(){
        return this.slotSpinPayouts.stream()
                .mapToInt(SlotSpinPayout::getPayoutAmount)
                .sum();
    }
}
