package com.davidklhui.slotgame.controller;

import com.davidklhui.slotgame.exception.SlotException;
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

    @GetMapping("/get/{slotId}")
    public Slot getSlotById(@PathVariable("slotId") final int slotId){
        final Optional<Slot> slotOptional = slotService.findSlotById(slotId);
        return slotOptional.orElseThrow(
                ()-> new SlotException(
                        String.format("Slot not found, given id = %d", slotId)
                )
        );
    }


}
