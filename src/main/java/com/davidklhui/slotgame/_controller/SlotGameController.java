package com.davidklhui.slotgame._controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slot")
public class SlotGameController {

    @GetMapping
    public Object test1(){
        return null;
    }

}
