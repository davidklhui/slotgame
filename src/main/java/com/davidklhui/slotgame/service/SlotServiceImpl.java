package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.PayoutDefinition;
import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.repository.SlotRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.info("slot: {}", slot);
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
        logic when adding payout definition

     */
    @Override
    public boolean addPayoutDefinition(final int slotId, final PayoutDefinition payoutDefinition){
        final Optional<Slot> existingSlotOptional = slotRepository.findById(slotId);
        if(existingSlotOptional.isPresent()){
            final Slot existingSlot = existingSlotOptional.get();
            existingSlot.addPayoutDefinition(payoutDefinition);

            slotRepository.save(existingSlot);

            return true;
        }
        return false;

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
        update slot should not update payout definition and reels at the same time
        so set payout definitions and reels here
     */
    private Slot updateSlot(final Slot slot){
        setSlotProperties(slot);
        Optional<Slot> existingRecord = slotRepository.findById(slot.getSlotId());
        if(existingRecord.isPresent()){
            slot.setPayoutDefinitions(existingRecord.get().getPayoutDefinitions());
            slot.setReels(existingRecord.get().getReels());
        }
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
