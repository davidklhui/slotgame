package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.Payline;

import java.util.List;
import java.util.Optional;

public interface IPaylineService {
    List<Payline> listPaylines();

    Optional<Payline> findPaylineById(int paylineId);
}
