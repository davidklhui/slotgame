package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.model.Symbol;
import com.davidklhui.slotgame.repository.SymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    @Override
    public Symbol saveSymbol(Symbol symbol) {

        return symbolRepository.save(symbol);
    }

    @Override
    public boolean deleteSymbol(int symbolId) {

        Optional<Symbol> symbolOptional = symbolRepository.findById(symbolId);
        if(symbolOptional.isEmpty()){
            return false;
        } else{
            symbolRepository.deleteById(symbolId);
            return true;
        }
    }
}
