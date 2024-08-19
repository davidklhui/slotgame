package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SlotServiceImpl implements ISlotService {

    private final SlotRepository slotRepository;

    @Autowired
    public SlotServiceImpl(final SlotRepository slotRepository){
        this.slotRepository = slotRepository;
    }


    @Override
    public List<Slot> listSlots() {
        return slotRepository.findAll();
    }

    @Override
    public Optional<Slot> findSlotById(int slotId) {
        return slotRepository.findById(slotId);
    }
}
