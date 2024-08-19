package com.davidklhui.slotgame.controller;


import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.model.SlotSpinResult;
import com.davidklhui.slotgame.service.ISlotGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/slotgame")
@Slf4j
public class SlotGameController {

    private final ISlotGameService slotGameService;

    @Autowired
    public SlotGameController(final ISlotGameService slotGameService){
        this.slotGameService = slotGameService;
    }

//    @PostMapping(value="/spin")
//    public SlotSpinResult spin(@RequestBody final SlotGameDefinition slotGameDefinition){
//        log.debug("{}", slotGameDefinition);
//        return slotGameService.spin(slotGameDefinition);
//    }

    @PostMapping(value = "/spin")
    public SlotSpinResult spin(@RequestBody final Slot slot){
        log.debug("{}", slot);
        return slotGameService.spin(slot);
    }


}
