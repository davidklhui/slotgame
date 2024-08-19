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

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = SlotGameApplication.class)
class SlotTest {

    private List<Reel> reels;

    @BeforeEach
    void preConfigReels(){
        Symbol symbol1 = new Symbol(1, "1", false);
        Symbol symbol2 = new Symbol(2, "2", false);
        Symbol symbol3 = new Symbol(3, "3", false);
        Symbol symbol4 = new Symbol(4, "4", false);
        Symbol symbol5 = new Symbol(5, "5", false);

        Reel reel1 = new Reel(
                Map.of(symbol1, BigDecimal.valueOf(0.1),
                        symbol2, BigDecimal.valueOf(0.2),
                        symbol3, BigDecimal.valueOf(0.6),
                        symbol4, BigDecimal.valueOf(0.1))
        );

        Reel reel2 = new Reel(
                Map.of(symbol1, BigDecimal.valueOf(0.1),
                        symbol2, BigDecimal.valueOf(0.2),
                        symbol3, BigDecimal.valueOf(0.6),
                        symbol4, BigDecimal.valueOf(0.1))
        );

        Reel reel3 = new Reel(
                Map.of(symbol1, BigDecimal.valueOf(0.1),
                        symbol2, BigDecimal.valueOf(0.9))
        );

        Reel reel4 = new Reel(
                Map.of(symbol1, BigDecimal.valueOf(0.2),
                        symbol2, BigDecimal.valueOf(0.2),
                        symbol3, BigDecimal.valueOf(0.2),
                        symbol4, BigDecimal.valueOf(0.2),
                        symbol5, BigDecimal.valueOf(0.2))
        );

        Reel reel5 = new Reel(
                Map.of(symbol1, BigDecimal.valueOf(0.002),
                        symbol2, BigDecimal.valueOf(0.001),
                        symbol3, BigDecimal.valueOf(0.001),
                        symbol4, BigDecimal.valueOf(0.996))
        );

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
        log.debug("Slot simulation outcome: {}", outcomes);

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
