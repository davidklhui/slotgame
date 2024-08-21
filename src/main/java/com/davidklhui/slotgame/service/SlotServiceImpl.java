package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.repository.SlotRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SlotServiceImpl implements ISlotService {

    private final SlotRepository slotRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public SlotServiceImpl(final SlotRepository slotRepository,
                           final EntityManager entityManager){
        this.slotRepository = slotRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<Slot> listSlots() {
        return slotRepository.findAll();
    }

    @Override
    public Optional<Slot> findSlotById(final int slotId) {
        return slotRepository.findById(slotId);
    }

    /*
        Note that when saving new/existing slot
        Symbols must be present
     */
    @Override
    public Slot saveSlot(Slot slot){
        /*
            check if slot id is null
            if so, then required to perform some steps
         */

        if(slot.getSlotId() == null){
            return createSlot(slot);
        } else {
            return updateSlot(slot);
        }
    }

    /*
        logic when creating slot
     */
    private Slot createSlot(Slot slot){
        setSlotProperties(slot);
        slot = slotRepository.save(slot);
        return refreshSlot(slot);

    }

    /*
        logic when updating slot
     */
    private Slot updateSlot(final Slot slot){
        setSlotProperties(slot);
        return slotRepository.save(slot);
    }

    private void setSlotProperties(final Slot slot){
        slot.getReels().forEach(
                reel-> {
                    reel.setSlot(slot);
                    reel.getSymbolProbSet()
                            .forEach(symbolProb -> symbolProb.setReel(reel));
                }
        );

    }

    /*
        run this refresh during insert to ensure the returned slot contains the information of other entities
        e.g. symbol name
        if not perform this refresh, then the these attributes are null
     */
    private Slot refreshSlot(Slot slot){
        entityManager.refresh(slot);
        return slot;
    }

}
