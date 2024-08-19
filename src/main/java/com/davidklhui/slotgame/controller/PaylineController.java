package com.davidklhui.slotgame.controller;

import com.davidklhui.slotgame.exception.PaylineException;
import com.davidklhui.slotgame.model.Payline;
import com.davidklhui.slotgame.service.IPaylineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payline")
public class PaylineController {

    private final IPaylineService paylineService;

    @Autowired
    public PaylineController(final IPaylineService paylineService){
        this.paylineService = paylineService;
    }

    @GetMapping("/list")
    public List<Payline> listPaylines(){
        return paylineService.listPaylines();
    }

    @GetMapping("/get/{paylineId}")
    public Payline getPayline(@PathVariable("paylineId") final int paylineId){
        Optional<Payline> paylineOptional = paylineService.findPaylineById(paylineId);
        if(paylineOptional.isPresent()){
            return paylineOptional.get();
        } else {
            throw new PaylineException(
                    String.format("Payline not found. Given id = %d", paylineId));
        }
    }

}
