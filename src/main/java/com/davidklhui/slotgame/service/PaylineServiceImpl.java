package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.Payline;
import com.davidklhui.slotgame.repository.PaylineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaylineServiceImpl implements IPaylineService {

    private final PaylineRepository paylineRepository;

    @Autowired
    public PaylineServiceImpl(final PaylineRepository paylineRepository){
        this.paylineRepository = paylineRepository;
    }

    @Override
    public List<Payline> listPaylines(){

        return paylineRepository.findAll();
    }

    @Override
    public Optional<Payline> findPaylineById(final int paylineId){
        return paylineRepository.findById(paylineId);
    }
}
