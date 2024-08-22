package com.davidklhui.slotgame.controller;

import com.davidklhui.slotgame.exception.SlotException;
import com.davidklhui.slotgame.model.PayoutDefinition;
import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.service.ISlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * This controller class handle slot definition
 */
@RestController
@RequestMapping("/slot")
public class SlotController {

    private final ISlotService slotService;

    @Autowired
    public SlotController(final ISlotService slotService){
        this.slotService = slotService;
    }

    @GetMapping("/list")
    public List<Slot> listSlots(){
        return slotService.listSlots();
    }

    @GetMapping("/{slotId}/get")
    public Slot getSlotById(@PathVariable("slotId") final int slotId){
        final Optional<Slot> slotOptional = slotService.findSlotById(slotId);
        if(slotOptional.isPresent()) {
            return slotOptional.get();
        } else {
            throw new SlotException(
                            String.format("Slot not found, given id = %d", slotId)
            );
        }
    }

    @PostMapping("/{slotId}/payout-defn/add")
    public boolean addPayoutDefinitionToSlot(
            @PathVariable("slotId") final int slotId,
            @RequestBody final PayoutDefinition payoutDefinition){

        return slotService.addPayoutDefinition(slotId, payoutDefinition);

    }


    @PostMapping("/save")
    public Slot saveSlot(@RequestBody final Slot slot){
        return slotService.saveSlot(slot);
    }


}
