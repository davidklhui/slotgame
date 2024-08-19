package com.davidklhui.slotgame.controller;

import com.davidklhui.slotgame.exception.SymbolException;
import com.davidklhui.slotgame.model.Symbol;
import com.davidklhui.slotgame.service.ISymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 *  This controller class handle list symbols or get specific symbol by id
 *
 */
@RestController
@RequestMapping("/symbol")
public class SymbolController {

    private final ISymbolService symbolService;

    @Autowired
    public SymbolController(final ISymbolService symbolService){
        this.symbolService = symbolService;
    }

    @GetMapping("/list")
    public List<Symbol> listSymbols(){
        return symbolService.listSymbols();
    }

    @GetMapping("/get/{symbolId}")
    public Symbol getSymbolById(@PathVariable("symbolId") final int symbolId){
        Optional<Symbol> symbolOptional = symbolService.findSymbolById(symbolId);

        return symbolOptional.orElseThrow(
                ()-> new SymbolException(
                        String.format("Symbol not found, given symbol id = %d", symbolId)
                )
        );
    }

}
