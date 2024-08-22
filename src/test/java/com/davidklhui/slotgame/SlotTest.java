package com.davidklhui.slotgame;


import com.davidklhui.slotgame.model.PayoutDefinition;
import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.model.Symbol;
import com.davidklhui.slotgame.service.IPaylineService;
import com.davidklhui.slotgame.service.IPayoutDefinitionService;
import com.davidklhui.slotgame.service.ISlotService;
import com.davidklhui.slotgame.service.ISymbolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-dev.yml")
@AutoConfigureMockMvc
@SpringBootTest(classes = SlotGameApplication.class)
@Transactional
@Slf4j
@SqlGroup({
        @Sql(scripts = "/insert_symbols.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "/insert_coordinates.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/insert_paylines.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/insert_slots_reels.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/insert_payouts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/delete_payouts.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/delete_slots_reels.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/delete_paylines.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/delete_coordinates.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/delete_symbols.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
class SlotTest {

    /**
     * This class test slot definition (Slot - Reel - Symbol)
     *
     * A: Service methods
     *      1. list slot
     *      2. get slot by id
     *      3. save slot
     *      4. add payout definition to existing slot
     *
     * B: REST API
     *      1. GET  /slot/list
     *      2. GET  /slot/{slotId}/get (existing / non-existing record)
     *      3. POST /slot/save (new / existing record)
     *      4. POST /slot/{slotId}/payout-defn/add
     *
     */

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ISlotService slotService;

    @Autowired
    private IPaylineService paylineService;

    @Autowired
    private ISymbolService symbolService;

    @Autowired
    private IPayoutDefinitionService payoutDefinitionService;

    @Value("classpath:save_existing_slot.json")
    private Resource existingSlotSampleJsonResource;

    /*
        new slot json file with reels configurations
     */
    @Value("classpath:save_new_slot.json")
    private Resource newSlotSampleJsonResource;

    /*
        another new slot json, but without reels configurations
     */
    @Value("classpath:save_new_slot2.json")
    private Resource newSlotSample2JsonResource;

    /**
     * A: Service methods
     *      1. list slot
     *      2. get slot by id
     *      3. save slot
     *      4. add payout definition to existing slot
     *
     */

    @Test
    void listSlotsTest(){
        // from the .sql file there is only 1 slot record
        List<Slot> slotList = slotService.listSlots();

        assertEquals(1, slotList.size(), "Slot list size = 1");

    }

    @Test
    void getSlotTest(){

        // from the .sql file there is only 1 slot record, which is id = 1
        Optional<Slot> existingSlot = slotService.findSlotById(1);
        assertTrue(existingSlot.isPresent(), "Slot id = 1 should exist");

        Optional<Slot> nonExistingSlot = slotService.findSlotById(0);
        assertFalse(nonExistingSlot.isPresent(), "Slot id = 0 should not exist");

    }

    @Test
    void addPayoutToSlotTest(){

        Optional<Slot> existingSlotOptional = slotService.findSlotById(1);
        assertTrue(existingSlotOptional.isPresent(), "Slot id = 1 should exist");

        Set<PayoutDefinition> definedPayouts = existingSlotOptional.get().getPayoutDefinitions();
        assertEquals(55, definedPayouts.size(), "Defined number of payouts = 55");


        PayoutDefinition newPayoutDefinition = new PayoutDefinition();
        // choose the first payline, this should exist
        newPayoutDefinition.setPayline(paylineService.findPaylineById(1).get());

        List<Symbol> symbolOrder = symbolService.listSymbols().stream().sorted(
                Comparator.comparing(Symbol::getSymbolId)).limit(5).toList();

        // set the symbols list to be 1,2,3,4,5
        newPayoutDefinition.setSymbols(symbolOrder);
        newPayoutDefinition.setPayoutAmount(12345);

        // add payout to slot id = 1
        boolean addResult = slotService.addPayoutDefinition(1, newPayoutDefinition);
        assertTrue(addResult);

        // get the data back again
        existingSlotOptional = slotService.findSlotById(1);
        Slot existingSlot = existingSlotOptional.get();
        Set<PayoutDefinition> updatedPayoutDefinitions = existingSlot.getPayoutDefinitions();
        assertEquals(56, updatedPayoutDefinitions.size(), "Defined number of payouts should be incremented to 56");

        // find out that payout and perform further assertion
        Optional<PayoutDefinition> thatPayoutDefnOptional = updatedPayoutDefinitions.stream()
                .filter(pd-> pd.getPayoutAmount() == 12345)
                .findFirst();

        assertTrue(thatPayoutDefnOptional.isPresent());

        PayoutDefinition thatPayoutDefn = thatPayoutDefnOptional.get();
        assertTrue(thatPayoutDefn.getPayoutId() > 0, "payout definition should now have id");
        assertEquals(12345, thatPayoutDefn.getPayoutAmount(), "amount should be 12345");
        assertIterableEquals(thatPayoutDefn.getSymbols(), symbolOrder, "Defined symbols should be equal");

    }


    /**
     * B: REST API
     *      1. GET  /slot/list
     *      2. GET  /slot/{slotId}/get (existing / non-existing record)
     *      3. POST /slot/save (new / existing record)
     *      4. POST /slot/{slotId}/payout-defn/add
     *
     */
    @Test
    void listSlotApiTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/slot/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getExistingSlotApiTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/slot/{slotId}/get", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.slotId", is(1)))
                .andExpect(jsonPath("$.slotName", is("Demo 1")))
                .andExpect(jsonPath("$.description", is("Demo 5x3 slot design")))
                .andExpect(jsonPath("$.numberOfRows", is(3)))
                .andExpect(jsonPath("$.reels", hasSize(5)));
    }

    @Test
    void getNonExistingSlotApiTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/slot/{slotId}/get", 0))
                .andExpect(status().is4xxClientError());

    }



    @Test
    void saveExistingSlotApiTest() throws Exception {
        // before save, check size of slots = 1
        List<Slot> slotList = slotService.listSlots();
        assertEquals(1, slotList.size(), "Slot list size = 1");

        Optional<Slot> slotId1 = slotService.findSlotById(1);
        assertTrue(slotId1.isPresent(), "Slot id = 1 should exists in DB");

        Slot slot = slotId1.get();
        assertEquals("Demo 1", slot.getSlotName(), "Slot id=1 name is 'Demo 1'");

        // read the json as string
        String jsonStr = existingSlotSampleJsonResource.getContentAsString(StandardCharsets.UTF_8);

        // perform POST
        mockMvc.perform(MockMvcRequestBuilders.post("/slot/save")
                        .contentType(APPLICATION_JSON)
                        .content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.slotId", is(1)))
                .andExpect(jsonPath("$.slotName", is("Demo 1 (Updated)")))
                .andExpect(jsonPath("$.numberOfRows", is(3)))
                .andExpect(jsonPath("$.numberOfReels", is(5)))
                .andExpect(jsonPath("$.reels", hasSize(5)));

        // after save, size should remain 1
        slotList = slotService.listSlots();
        assertEquals(1, slotList.size(), "Slot list size remains 1 after inserted");

        // retrieve from DB, and check if it exists
        slotId1 = slotService.findSlotById(1);
        assertTrue(slotId1.isPresent(), "Slot id = 1 should still exist in DB");
        slot = slotId1.get();
        assertEquals("Demo 1 (Updated)", slot.getSlotName(), "Slot id=1 name is updated to 'Demo 1 (Updated)'");

        // confirm payout definitions still have size = 55
        assertEquals(55, slot.getPayoutDefinitions().size(), "Slot id=1 payout definitions size should remain 55");
    }




    /*
        save the file with reels configuration
     */
    @Test
    void saveNewSlotWithReelsApiTest() throws Exception {
        // before save, check size of slots = 1
        List<Slot> slotList = slotService.listSlots();
        assertEquals(1, slotList.size(), "Slot list size = 1");

        // read the json as string
        String jsonStr = newSlotSampleJsonResource.getContentAsString(StandardCharsets.UTF_8);

        // perform POST
        // slotId is expected to be 2, if unsure, use jsonPath("$.slotId").exists() instead
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/slot/save")
                    .contentType(APPLICATION_JSON)
                    .content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.slotId").value(greaterThan(0)))
                .andExpect(jsonPath("$.slotName", is("Demo 2")))
                .andExpect(jsonPath("$.numberOfRows", is(4)))
                .andExpect(jsonPath("$.numberOfReels", is(7)))
                .andExpect(jsonPath("$.reels", hasSize(5)))
                .andReturn();

        // after save, check size of slots = 2
        slotList = slotService.listSlots();
        assertEquals(2, slotList.size(), "Slot list size = 2 after inserted");

        Slot slotFromResponse = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), Slot.class);

        // retrieve from DB, and check if it exists
        Optional<Slot> slotInDB = slotService.findSlotById(slotFromResponse.getSlotId());
        assertTrue(slotInDB.isPresent(), "New Slot should exists in DB");

    }


    /*
        save the file without reels configuration
     */
    @Test
    void saveNewSlotWithoutReelsApiTest() throws Exception {
        // before save, check size of slots = 1
        List<Slot> slotList = slotService.listSlots();
        assertEquals(1, slotList.size(), "Slot list size = 1");

        // read the json as string
        String jsonStr = newSlotSample2JsonResource.getContentAsString(StandardCharsets.UTF_8);

        // perform POST
        // slotId is expected to be 2, if unsure, use jsonPath("$.slotId").exists() instead
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/slot/save")
                        .contentType(APPLICATION_JSON)
                        .content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.slotId").value(greaterThan(0)))
                .andExpect(jsonPath("$.slotName", is("Demo 3")))
                .andExpect(jsonPath("$.description", is("Test save without reels")))
                .andExpect(jsonPath("$.numberOfRows", is(12)))
                .andExpect(jsonPath("$.numberOfReels", is(34)))
                .andExpect(jsonPath("$.reels", hasSize(0)))
                .andReturn();

        // after save, check size of slots = 2
        slotList = slotService.listSlots();
        assertEquals(2, slotList.size(), "Slot list size = 2 after inserted");

        Slot slotFromResponse = OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), Slot.class);

        // retrieve from DB, and check if it exists
        Optional<Slot> slotInDB = slotService.findSlotById(slotFromResponse.getSlotId());
        assertTrue(slotInDB.isPresent(), "New Slot should exists in DB");

    }











}
