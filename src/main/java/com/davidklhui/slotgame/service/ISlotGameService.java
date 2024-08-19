package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.model.SlotGameDefinition;
import com.davidklhui.slotgame.model.SlotSpinResult;

public interface ISlotGameService {

    SlotSpinResult spin(SlotGameDefinition slotGameDefinition);

    SlotSpinResult spin(Slot slot);
}
