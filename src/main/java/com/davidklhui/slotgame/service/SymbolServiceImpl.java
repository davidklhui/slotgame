package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.Symbol;
import com.davidklhui.slotgame.repository.SymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SymbolServiceImpl implements ISymbolService {

    private final SymbolRepository symbolRepository;

    @Autowired
    public SymbolServiceImpl(final SymbolRepository symbolRepository){
        this.symbolRepository = symbolRepository;
    }


    @Override
    public List<Symbol> listSymbols() {
        return symbolRepository.findAll();
    }

    @Override
    public Optional<Symbol> findSymbolById(int symbolId) {
        return symbolRepository.findById(symbolId);
    }
}
