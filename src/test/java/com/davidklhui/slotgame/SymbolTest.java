package com.davidklhui.slotgame;

import com.davidklhui.slotgame.model.Symbol;
import com.davidklhui.slotgame.service.ISymbolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
    /**
      Involved test:
      A: Service Test
            1. check if the list size is the same as what we've inserted
            2. exist/ not exists symbol
            3. create symbol

      B: REST API Test:
            1. GET /symbol/list
            2. GET /symbol/get/{symbolId}
            3. POST /symbol/save
            4. DELETE /symbol/delete/{symbolId}
   */

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ISymbolService symbolService;


    /**
     * A: Service Test
     */



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


    /**
     * B: REST API Test
     */
    @Test
    void listSymbolApiTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/symbol/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(6)));

    }

    @Test
    void getSymbolApiTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/symbol/{symbolId}/get", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.symbolId", is(2)))
                .andExpect(jsonPath("$.symbolName", is("Lemon")))
                .andExpect(jsonPath("$.wild",is(false)));

        mockMvc.perform(MockMvcRequestBuilders.get("/symbol/{symbolId}/get", 0))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void saveNewSymbolApiTest() throws Exception {
        // initial list size
        int existingSymbolSize = symbolService.listSymbols().size();
        assertEquals(6, existingSymbolSize);

        // create a new symbol
        Symbol newSymbol = new Symbol("New Symbol", true);

        // perform POST
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/symbol/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(newSymbol)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.symbolName", is("New Symbol")))
                .andExpect(jsonPath("$.wild", is(true)))
                .andReturn();

        // check the size after save
        assertEquals(7, symbolService.listSymbols().size(), "Added 1 item, should have size 7");

        // parse the response JSON and find the symbol id, and see if it exists in DB
        String responseJson = mvcResult.getResponse().getContentAsString();
        Symbol symbolFromResponseJson = OBJECT_MAPPER.readValue(responseJson, Symbol.class);
        Optional<Symbol> checkInDb = symbolService.findSymbolById(symbolFromResponseJson.getSymbolId());
        assertTrue(checkInDb.isPresent(), "Should have existed in DB after creation");

    }

    @Test
    void saveExistingSymbolApiTest() throws Exception {
        // update an existing symbol
        Optional<Symbol> existingSymbolOptional = symbolService.findSymbolById(2);
        assertTrue(existingSymbolOptional.isPresent());
        Symbol existingSymbol = existingSymbolOptional.get();
        assertEquals("Lemon", existingSymbol.getSymbolName(), "Id 2 symbol name is Lemon");

        // update the name, and perform POST
        existingSymbol.setSymbolName("Watermelon");
        mockMvc.perform(MockMvcRequestBuilders.post("/symbol/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(existingSymbol)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.symbolId", is(2)))
                .andExpect(jsonPath("$.symbolName", is("Watermelon")))
                .andExpect(jsonPath("$.wild", is(false)));

        Optional<Symbol> checkInDb = symbolService.findSymbolById(2);

        assertEquals(6, symbolService.listSymbols().size(), "Saving existing item, size should remainl 6");
        assertEquals("Watermelon", checkInDb.get().getSymbolName());


    }

    @Test
    void deleteSymbolApiTest() throws Exception {

        // initial list size
        int existingSymbolSize = symbolService.listSymbols().size();
        assertEquals(6, existingSymbolSize);

        // delete existing symbol
        // confirm it exists
        Optional<Symbol> symbolId1 = symbolService.findSymbolById(1);
        assertTrue(symbolId1.isPresent());


        mockMvc.perform(MockMvcRequestBuilders.delete("/symbol/{symbolId}/delete", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));

        Optional<Symbol> symbolId0 = symbolService.findSymbolById(0);
        assertTrue(symbolId0.isEmpty());
        assertEquals(5, symbolService.listSymbols().size(), "Deleted 1 item, should have size 5");


        // delete non-existing symbol
        mockMvc.perform(MockMvcRequestBuilders.delete("/symbol/{symbolId}/delete", 0))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(false)));
        assertEquals(5, symbolService.listSymbols().size(), "Did not delete anything, should still have size 5");

    }



}
