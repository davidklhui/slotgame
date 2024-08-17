package com.davidklhui.slotgame;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@SpringBootTest(classes = SlotGameApplication.class)
class PaylineApiTest {

    @Autowired
    private MockMvc mockMvc;

    /*
        currently has 4 defined. Later once I have refactored to support db connection
        this can also be tested more dynamically
     */
    @Test
    void listPaylinesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/payline/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void getPaylineByIdTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/payline/get/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.paylineId", is(1)))
                .andExpect(jsonPath("$.paylineName", is("horizontal line 1")))
                .andExpect(jsonPath("$.paylineCoordinates", hasSize(3)));

        mockMvc.perform(MockMvcRequestBuilders.get("/payline/{id}", 0))
                .andExpect(status().is4xxClientError());
    }
}
