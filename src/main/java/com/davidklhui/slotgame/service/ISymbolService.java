package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.Symbol;

import java.util.List;
import java.util.Optional;

public interface ISymbolService {

    List<Symbol> listSymbols();

    Optional<Symbol> findSymbolById(final int symbolId);
}
