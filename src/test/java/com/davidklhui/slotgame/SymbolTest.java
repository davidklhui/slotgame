package com.davidklhui.slotgame;

import com.davidklhui.slotgame.model.Symbol;
import com.davidklhui.slotgame.service.ISymbolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@TestPropertySource("/application-dev.yml")
@SpringBootTest(classes = SlotGameApplication.class)
@Transactional
@Slf4j
@SqlGroup({
        @Sql(scripts = "/insert_symbols.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "/delete_symbols.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
class SymbolTest {
    /*
      Involved test:
      1. check if the list size is the same as what we've inserted
   */

    @Autowired
    private ISymbolService symbolService;


    @Test
    void checkCorrectSymbolListSizeTest(){

        List<Symbol> symbolList = symbolService.listSymbols();
        assertEquals(6, symbolList.size(), "Correct list size");
    }

    @Test
    void findExistAndNotExistSymbolTest(){

        Optional<Symbol> existingSymbol = symbolService.findSymbolById(1);
        assertTrue(existingSymbol.isPresent(), "Symbol id = 1 exists");

        Optional<Symbol> nonExistingSymbol = symbolService.findSymbolById(0);
        assertFalse(nonExistingSymbol.isPresent(), "Symbol id = 0 does not exists");
    }


}
