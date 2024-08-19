package com.davidklhui.slotgame;

import com.davidklhui.slotgame.model.Symbol;
import com.davidklhui.slotgame.service.ISymbolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureMockMvc
@TestPropertySource("/application-dev.yml")
@SpringBootTest(classes = SlotGameApplication.class)
@Transactional
@Slf4j
@SqlGroup({
        @Sql(scripts = "/insert_symbols.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/delete_symbols.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class SymbolTest {
    /*
      Involved test:
      A: Service Test
            1. check if the list size is the same as what we've inserted
            2. exist/ not exists symbol
            3. create symbol

      B: REST API Test:

   */

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ISymbolService symbolService;


    @Test
    void checkCorrectSymbolListSizeTest(){

        List<Symbol> symbolList = symbolService.listSymbols();
        assertEquals(6, symbolList.size(), "correct list size = 6");
    }

    @Test
    void findExistAndNotExistSymbolTest(){

        Optional<Symbol> existingSymbol = symbolService.findSymbolById(1);
        assertTrue(existingSymbol.isPresent(), "Symbol id = 1 exists");

        Optional<Symbol> nonExistingSymbol = symbolService.findSymbolById(0);
        assertFalse(nonExistingSymbol.isPresent(), "Symbol id = 0 does not exist");
    }

    @Test
    void addNewSymbolTest(){

        // according to the .sql file, the size should be 6
        List<Symbol> existingSymbols = symbolService.listSymbols();
        assertEquals(6, existingSymbols.size(), "Existing number of symbols is 6");

        // define and save new symbol
        Symbol newSymbol = new Symbol( "New Symbol", true);
        newSymbol = symbolService.saveSymbol(newSymbol);

        // confirm the newly added symbol is in DB
        Optional<Symbol> getSymbolFromDb = symbolService.findSymbolById(newSymbol.getSymbolId());
        assertTrue(getSymbolFromDb.isPresent(), "Newly added data required to be in the DB");

        // re-fetch the list
        existingSymbols = symbolService.listSymbols();
        assertEquals(7, existingSymbols.size(), "Existing number of symbols increased by 1, so should be 7");

    }


    @Test
    void deleteSymbolTest(){

        // according to the .sql file, the size should be 6
        List<Symbol> existingSymbols = symbolService.listSymbols();
        assertEquals(6, existingSymbols.size(), "Existing number of symbols is 6");

        // remove existing symbol
        Symbol symbolToBeDeleted = existingSymbols.stream().findAny().get();
        boolean isDeleted = symbolService.deleteSymbol(symbolToBeDeleted.getSymbolId());
        assertTrue(isDeleted, "The value should be true because the selected symbol is deleted");
        assertEquals(5, symbolService.listSymbols().size(), "Deleted one symbol, size should be 5");

        // try to delete a non-existing symbol
        isDeleted = symbolService.deleteSymbol(0);
        assertFalse(isDeleted, "The value should be false because id=0 not existed in DB");

    }



}
