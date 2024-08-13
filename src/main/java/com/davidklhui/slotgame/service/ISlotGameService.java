package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.PayoutDefinition;
import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.model.SlotGameDefinition;
import com.davidklhui.slotgame.model.SlotSpinResult;

import java.util.List;

public interface ISlotGameService {

    SlotSpinResult spin(SlotGameDefinition slotGameDefinition);
}
