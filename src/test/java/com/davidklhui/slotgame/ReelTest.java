package com.davidklhui.slotgame;

import com.davidklhui.slotgame.exception.ReelException;
import com.davidklhui.slotgame.model.Reel;
import com.davidklhui.slotgame.model.Symbol;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SlotGameApplication.class)
class ReelTest {

    @Test
    void correctReelTest(){

        Set<Symbol> symbols = new HashSet<>();
        symbols.add(new Symbol(1, "a", 0.1, true));
        symbols.add(new Symbol(2, "b", 0.9, true));

        assertDoesNotThrow(
                ()-> new Reel(symbols),
                "Correct Reel configuration"
        );

        Reel reel = new Reel(symbols);

        // valid size
        assertEquals(2, reel.getSymbols().size(), "Size is correct: 2");

        // correct total probability
        assertTrue((Boolean) ReflectionTestUtils.invokeMethod(reel, "isTotalProbabilitySumToOne"));

        // correct total probability
        assertEquals(BigDecimal.ONE.setScale(9), ReflectionTestUtils.invokeMethod(reel, "totalProbability"));

    }

    @Test
    void symbolsSizeLessThan2Test(){

        Set<Symbol> symbols = new HashSet<>();

        assertThrows(
                ReelException.class,
                ()-> new Reel(symbols),
                "Given Symbol Set size < 2"
        );

        symbols.add(new Symbol(1, "a", 0.1, true));

        assertThrows(
                ReelException.class,
                ()-> new Reel(symbols),
                "Given Symbol Set size < 2"
        );
    }

    @Test
    void symbolsTotalProbabilityIncorrectTest(){


        Set<Symbol> symbols = new HashSet<>();

        assertThrows(
                ReelException.class,
                ()-> new Reel(symbols),
                "Given Symbol Set size < 2"
        );

        symbols.add(new Symbol(1, "a", 0.1, true));
        symbols.add(new Symbol(2, "a", 0.2, true));

        assertThrows(
                ReelException.class,
                ()-> new Reel(symbols),
                "Total Probability is not 1"
        );

        symbols.add(new Symbol(3, "b", 0.699999999, true));
        assertDoesNotThrow(()-> new Reel(symbols), "Total Probability is slightly less than 1");

        assertEquals(BigDecimal.ONE.setScale(9),
                ReflectionTestUtils.invokeMethod(new Reel(symbols), "totalProbability"));

        symbols.add(new Symbol(4, "b", 0.000000001, true));
        assertDoesNotThrow(()-> new Reel(symbols), "Total Probability is slightly less than 1");

        symbols.add(new Symbol(5, "b", 0.000001, true));
        assertThrows(ReelException.class,
                ()-> new Reel(symbols),
                "Total Probability exceeded 1 a lot");

    }

    @Test
    void simSizeTest(){

        Set<Symbol> symbols = new HashSet<>();

        symbols.add(new Symbol(1, "a", 0.1, true));
        symbols.add(new Symbol(2, "a", 0.2, true));
        symbols.add(new Symbol(3, "a", 0.7, true));

        Reel reel = new Reel(symbols);

        assertEquals(1, reel.simulate(1).size(), "Simulate Outcome Size correct");
        assertEquals(4, reel.simulate(4).size(), "Simulate Outcome Size correct");

        assertThrows(
                ReelException.class,
                () -> reel.simulate(0),
                "Cannot simulate with outcome size = 0"
        );

    }




}

