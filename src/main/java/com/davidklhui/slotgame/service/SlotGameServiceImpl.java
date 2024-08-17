package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.exception.PaylineException;
import com.davidklhui.slotgame.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SlotGameServiceImpl implements ISlotGameService {

    /*
        Given the slot game definition, then perform a spin and return the result:
        1. the result will include the matched payout information (i.e. which payline, and payout amount)
        2. total payout return to the player
     */
    @Override
    public SlotSpinResult spin(final SlotGameDefinition slotGameDefinition){

        final Slot slot = slotGameDefinition.getSlot();
        final List<PayoutDefinition> payoutDefinitions = slotGameDefinition.getPayoutDefinitions();

        final List<List<Symbol>> outcomes = slot.spin();

        return slotSpinResult(outcomes, payoutDefinitions);
    }

    /*
        Given the slot game definition, then perform a spin and return the result:
        1. the result will include the matched payout information (i.e. which payline, and payout amount)
        2. total payout return to the player
     */
    private SlotSpinResult slotSpinResult(final List<List<Symbol>> outcomes,
                                          final List<PayoutDefinition> payoutDefinitions){

        final List<SlotSpinPayout> payouts = payoutDefinitions.stream()
                .map(payoutDefinition -> payoutForSpecificPayline(outcomes, payoutDefinition))
                .filter(payout-> payout.getPayoutAmount() > 0)
                .toList();

        return new SlotSpinResult(outcomes, payouts);

    }

    /*
        calculate the payout regardless of matched or not
        if matched -> payout amount > 0
        else -> payout amount = 0
     */
    private SlotSpinPayout payoutForSpecificPayline(final List<List<Symbol>> outcomes,
                                                    final PayoutDefinition payoutDefinition){

        final int payoutAmount = calculatePayout(
                                extractOutcomeSymbolByCoordinates(outcomes, payoutDefinition.getPayline().getPaylineCoordinates()),
                                payoutDefinition);

        return new SlotSpinPayout(payoutDefinition, payoutAmount);
    }

    /*
        helper function, extract the symbols from the given outcomes by the coordinates list
     */
    private List<Symbol> extractOutcomeSymbolByCoordinates(final List<List<Symbol>> outcomes,
                                                            final List<PaylineCoordinate> coordinates){

        return coordinates.stream()
                .map(coordinate-> {
                    if(outcomes.size() > coordinate.getReelIndex() &&
                            outcomes.get(coordinate.getReelIndex()).size() > coordinate.getRowIndex()) {

                        return outcomes.get(coordinate.getReelIndex()).get(coordinate.getRowIndex());
                    } else {
                        throw new PaylineException(
                                String.format("Invalid payline: given dimension is: (%d, %d), requiring (%d, %d)",
                                        outcomes.size(),
                                        outcomes.getFirst().size(),
                                        coordinate.getReelIndex(),
                                        coordinate.getRowIndex()));
                    }
                }).toList();
    }

    /*
        calculate the payout
        if matched -> return the payout; else 0

     */
    private int calculatePayout(final List<Symbol> outcomeSymbols,
                                final PayoutDefinition payoutDefinition){

        return matched(outcomeSymbols, payoutDefinition.getSymbols()) ? payoutDefinition.getPayoutAmount(): 0;
    }


    /*
        check if a matching occurred
     */
    private boolean matched(final List<Symbol> outcomeSymbols,
                            final List<Symbol> expectedSymbols){

        if(outcomeSymbols.size() != expectedSymbols.size()) {
            return false;
        } else {
            for (int i = 0; i < outcomeSymbols.size(); i++) {
                if (! outcomeSymbols.get(i).equals(expectedSymbols.get(i))) {
                    return false;
                }
            }
            // only can reach this line if all of them are true
            return true;
        }

    }


}
