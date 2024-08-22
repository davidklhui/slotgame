package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.PayoutDefinition;
import com.davidklhui.slotgame.model.Slot;

import java.util.List;
import java.util.Optional;

public interface ISlotService {

    List<Slot> listSlots();

    Optional<Slot> findSlotById(final int slotId);

    Slot saveSlot(Slot slot);

    boolean addPayoutDefinition(int slotId, PayoutDefinition payoutDefinition);
}
