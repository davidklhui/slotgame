package com.davidklhui.slotgame;

import com.davidklhui.slotgame.exception.SlotException;
import com.davidklhui.slotgame.model.Reel;
import com.davidklhui.slotgame.model.Slot;
import com.davidklhui.slotgame.model.Symbol;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = SlotGameApplication.class)
class SlotTest {
    
    private List<Reel> reels;

    @BeforeEach
    void preConfigReels(){
        
        Reel reel1 = new Reel(
                new HashSet<>(Arrays.asList(new Symbol(1, "1", 0.1, false),
                        new Symbol(2, "2", 0.2, false),
                        new Symbol(3, "3", 0.7, false))));
        
        Reel reel2 = new Reel(
                new HashSet<>(Arrays.asList(new Symbol(1, "1", 0.1, false),
                        new Symbol(2, "2", 0.2, false),
                        new Symbol(3, "3", 0.6, false),
                        new Symbol(4, "4", 0.1, false))));

        Reel reel3 = new Reel(
                new HashSet<>(Arrays.asList(new Symbol(1, "1", 0.1, false),
                        new Symbol(2, "2", 0.9, false))));

        Reel reel4 = new Reel(
                new HashSet<>(Arrays.asList(new Symbol(1, "1", 0.2, false),
                        new Symbol(2, "2", 0.2, false),
                        new Symbol(3, "3", 0.2, false),
                        new Symbol(4, "4", 0.2, false),
                        new Symbol(5, "5", 0.2, false))));

        Reel reel5 = new Reel(
                new HashSet<>(Arrays.asList(new Symbol(1, "1", 0.002, false),
                        new Symbol(2, "2", 0.001, false),
                        new Symbol(3, "3", 0.001, false),
                        new Symbol(4, "4", 0.996, false))));

        reels = Arrays.asList(reel1, reel2, reel3, reel4, reel5);

    }

    @AfterEach
    void clearReels(){
        reels = null;
    }

    @Test
    void slotCorrectConfigTest(){
        int numberOfRows = 3;

        assertDoesNotThrow(
                ()-> new Slot(reels, numberOfRows),
                "Does not throw during construction of slot"
        );

        Slot slot = new Slot(reels, numberOfRows);

        // check have correct number of reels
        assertEquals(5, slot.getNumberOfReels(), "Reels has size 5");

        // perform simulation
        List<List<Symbol>> outcomes = slot.spin();

        // check if it has the correct dimension
        // outer is expected to have 5, inner have 3
        assertEquals(5, outcomes.size(), "Outer size should be 5");

        outcomes.forEach(outcome-> {
            assertEquals(numberOfRows, outcome.size(), "Inner size should be 3");
        });

        // for reassurance, take a look at the outcome
        log.info("Slot simulation outcome: {}", outcomes);

    }
    

    @Test
    void incorrectSlotConfgTest(){

        // incorrect num of columns (reels)
        // must have at least 3
        List<Reel> reelsWithSize0 = new ArrayList<>();
        assertThrows(
                SlotException.class,
                ()-> new Slot(reelsWithSize0, 3),
                "Slot must not num reels less than 3"
        );

        List<Reel> reelsWithSize1 = reels.subList(0, 1);
        assertThrows(
                SlotException.class,
                ()-> new Slot(reelsWithSize1, 3),
                "Slot must not num reels less than 3"
        );

        List<Reel> reelsWithSize2 = reels.subList(0, 1);
        assertThrows(
                SlotException.class,
                ()-> new Slot(reelsWithSize2, 3),
                "Slot must not num reels less than 3"
        );


        // this will not throw because supplied reel size = 3
        List<Reel> reelsWithSize3 = reels.subList(0, 3);
        assertDoesNotThrow(
                ()-> new Slot(reelsWithSize3, 3),
                "Slot have num reels at least 3"
        );


        // incorrect num of rows (<=0)
        assertThrows(
                SlotException.class,
                ()-> new Slot(reels, 0),
                "Slot must not have num rows = 0"
        );


    }
    
}
