package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.Payline;

import java.util.List;

public interface IPaylineService {
    List<Payline> listPaylines();

    Payline findPaylineById(int id);
}
