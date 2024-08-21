package com.davidklhui.slotgame;


import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.service.ISlotService;
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
        @Sql(scripts = "/insert_slots_reels.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/delete_slots_reels.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/delete_symbols.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
class SlotTest {

    /**
     * This class test slot definition (Slot - Reel - Symbol)
     *
     * A: Service
     *
     * B: REST API
     *
     */

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ISlotService slotService;

    @Value("classpath:save_new_slot.json")
    private Resource newSlotSampleJsonResource;

    @Value("classpath:save_existing_slot.json")
    private Resource existingSlotSampleJsonResource;

    /**
        A: Service methods
            1. list slot
            2. get slot by id
            3. save slot
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


    /**
        B: REST API
            1. GET  /slot/list
            2. GET  /slot/get/{slotId} (existing / non-existing record)
            3. POST /slot/save (new record)
            4. POST /slot/save (existing record)
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

        mockMvc.perform(MockMvcRequestBuilders.get("/slot/get/{slotId}", 1))
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

        mockMvc.perform(MockMvcRequestBuilders.get("/slot/get/{slotId}", 0))
                .andExpect(status().is4xxClientError());

    }

    @Test
    void saveNewSlotApiTest() throws Exception {
        // before save, check size of slots = 1
        List<Slot> slotList = slotService.listSlots();
        assertEquals(1, slotList.size(), "Slot list size = 1");

        Optional<Slot> slotId2 = slotService.findSlotById(2);
        assertTrue(slotId2.isEmpty(), "Slot id = 2 should not exists in DB");

        // read the json as string
        String jsonStr = newSlotSampleJsonResource.getContentAsString(StandardCharsets.UTF_8);

        // perform POST
        // slotId is expected to be 2, if unsure, use jsonPath("$.slotId").exists() instead
        mockMvc.perform(MockMvcRequestBuilders.post("/slot/save")
                    .contentType(APPLICATION_JSON)
                    .content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.slotId", is(2)))
                .andExpect(jsonPath("$.slotName", is("Demo 2")))
                .andExpect(jsonPath("$.numberOfRows", is(4)))
                .andExpect(jsonPath("$.reels", hasSize(3)));

        // after save, check size of slots = 2
        slotList = slotService.listSlots();
        assertEquals(2, slotList.size(), "Slot list size = 2 after inserted");

        // retrieve from DB, and check if it exists
        slotId2 = slotService.findSlotById(2);
        assertTrue(slotId2.isPresent(), "Slot id = 2 should exists in DB");

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
        // slotId is expected to be 2, if unsure, use jsonPath("$.slotId").exists() instead
        mockMvc.perform(MockMvcRequestBuilders.post("/slot/save")
                        .contentType(APPLICATION_JSON)
                        .content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.slotId", is(1)))
                .andExpect(jsonPath("$.slotName", is("Demo 1 (Updated)")))
                .andExpect(jsonPath("$.numberOfRows", is(3)))
                .andExpect(jsonPath("$.reels", hasSize(5)));

        // after save, size should remain 1
        slotList = slotService.listSlots();
        assertEquals(1, slotList.size(), "Slot list size remains 1 after inserted");

        // retrieve from DB, and check if it exists
        slotId1 = slotService.findSlotById(1);
        assertTrue(slotId1.isPresent(), "Slot id = 1 should still exist in DB");
        slot = slotId1.get();
        assertEquals("Demo 1 (Updated)", slot.getSlotName(), "Slot id=1 name is updated to 'Demo 1 (Updated)'");

    }

















}
