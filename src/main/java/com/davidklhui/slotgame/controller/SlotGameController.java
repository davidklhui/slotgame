package com.davidklhui.slotgame.controller;


import com.davidklhui.slotgame.model.SlotGameDefinition;
import com.davidklhui.slotgame.model.SlotSpinResult;
import com.davidklhui.slotgame.service.ISlotGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PostMapping(value="/spin")
    public SlotSpinResult spin(@RequestBody SlotGameDefinition slotGameDefinition){
        log.info("{}", slotGameDefinition);
        return slotGameService.spin(slotGameDefinition);
    }


}
