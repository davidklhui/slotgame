package com.davidklhui.slotgame;

import com.davidklhui.slotgame.model.Payline;
import com.davidklhui.slotgame.service.IPaylineService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@TestPropertySource("/application-dev.yml")
@SpringBootTest(classes = SlotGameApplication.class)
@Transactional
@Slf4j
@SqlGroup({
        @Sql(scripts = "/insert_coordinates.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "/insert_paylines.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "/delete_paylines.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS),
        @Sql(scripts = "/delete_coordinates.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
class PaylineTest {

    /**
        Involved tests:
        A: Service Functionality
            1. check if the payline list size is the same as what we've inserted
            2. check if getting payline id = 1 success, but 0 failed
        B: Rest API
            1. test endpoint GET /payline/list
            2. test endpoint GET /payline/get/{id}
     */



    @Autowired
    private IPaylineService paylineService;

    @Autowired
    private MockMvc mockMvc;

    /*
        A: Service Functionality

     */
    @Test
    void checkPaylineCorrectListSizeTest(){

        final List<Payline> paylineList = paylineService.listPaylines();
        assertEquals(11, paylineList.size(), "Correct Payline list size");
    }

    @Test
    void checkGetPaylineByIdTest(){

        final Optional<Payline> existPayline = paylineService.findPaylineById(1);
        assertTrue(existPayline.isPresent(), "Payline id = 1 exists");

        final Optional<Payline> notExistsPayline = paylineService.findPaylineById(0);
        assertTrue(notExistsPayline.isEmpty(), "Payline id = 0 not exists");

    }

    /*
        B: Rest API
     */
    @Test
    void listPaylinesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/payline/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(11)));
    }

    @Test
    void getPaylineByIdTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/payline/get/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.paylineId", is(1)))
                .andExpect(jsonPath("$.paylineName", is("horizontal line 1")))
                .andExpect(jsonPath("$.coordinates", hasSize(5)));

        mockMvc.perform(MockMvcRequestBuilders.get("/payline/{id}", 0))
                .andExpect(status().is4xxClientError());
    }
}