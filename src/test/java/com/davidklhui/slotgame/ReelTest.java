package com.davidklhui.slotgame;

import com.davidklhui.slotgame.exception.ReelException;
import com.davidklhui.slotgame.model.Reel;
import com.davidklhui.slotgame.model.Symbol;
import com.davidklhui.slotgame.model.SymbolProb;
import com.davidklhui.slotgame.service.ISymbolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-dev.yml")
@SpringBootTest(classes = SlotGameApplication.class)
@Transactional
@Slf4j
@SqlGroup({
        @Sql(scripts = "/insert_symbols.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "/delete_symbols.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
class ReelTest {

    @Autowired
    private ISymbolService symbolService;

    @Test
    void correctReelTest(){

        Symbol symbol1 = symbolService.findSymbolById(1).get();
        Symbol symbol2 = symbolService.findSymbolById(2).get();

        Set<SymbolProb> symbolProbSet = new Reel.SymbolMapBuilder()
                                                        .put(symbol1, 0.1)
                                                        .put(symbol2, 0.9)
                                                        .build();

        assertDoesNotThrow(
                ()-> new Reel(symbolProbSet),
                "Correct Reel configuration"
        );

        Reel reel = new Reel(symbolProbSet);

        // valid size
        assertEquals(2, reel.numberOfSymbols(), "correct size is 2");

        // correct total probability
        assertTrue((Boolean) ReflectionTestUtils.invokeMethod(reel, "isTotalProbabilitySumToOne"));

        // correct total probability
        assertEquals(BigDecimal.ONE.setScale(9), ReflectionTestUtils.invokeMethod(reel, "totalProbability"));

    }

    @Test
    void symbolsSizeLessThan2Test(){

        Map<Symbol, BigDecimal> symbolProbSet = new HashMap<>();
        assertThrows(
                ReelException.class,
                ()-> new Reel(symbolProbSet),
                "Given Symbol Set size < 2"
        );


        symbolProbSet.put(symbolService.findSymbolById(1).get(),
                        BigDecimal.ONE);

        assertThrows(
                ReelException.class,
                ()-> new Reel(symbolProbSet),
                "Given Symbol Set size < 2"
        );
    }

    @Test
    void symbolsTotalProbabilityIncorrectTest(){

        Symbol symbol1 = symbolService.findSymbolById(1).get();
        Symbol symbol2 = symbolService.findSymbolById(2).get();
        Symbol symbol3 = symbolService.findSymbolById(3).get();
        Symbol symbol4 = symbolService.findSymbolById(4).get();
        Symbol symbol5 = symbolService.findSymbolById(5).get();

        Map<Symbol, BigDecimal> emptyMap = new HashMap<>();

        assertThrows(
                ReelException.class,
                ()-> new Reel(emptyMap),
                "Given Symbol Set size < 2"
        );


        final Set<SymbolProb> symbolProbSet1 = new Reel.SymbolMapBuilder()
                        .put(symbol1, 0.1)
                        .put(symbol2, 0.2)
                        .build();

        assertThrows(
                ReelException.class,
                ()-> new Reel(symbolProbSet1),
                "Total Probability is not 1"
        );

        final Set<SymbolProb> symbolProbSet2 = new Reel.SymbolMapBuilder()
                                                        .put(symbol1, 0.1)
                                                        .put(symbol2, 0.2)
                                                        .put(symbol3, 0.69999999999)
                                                        .build();

        assertDoesNotThrow(()-> new Reel(symbolProbSet2), "Total Probability is not slightly less than 1");

        assertEquals(BigDecimal.ONE.setScale(9),
                ReflectionTestUtils.invokeMethod(new Reel(symbolProbSet2), "totalProbability"));


        final Set<SymbolProb> symbolProbSet3 = new Reel.SymbolMapBuilder()
                                                    .put(symbol1, 0.1)
                                                    .put(symbol2, 0.2)
                                                    .put(symbol3, 0.69999999999)
                                                    .put(symbol4, 0.00000000001)
                                                    .build();

        assertDoesNotThrow(()-> new Reel(symbolProbSet3), "Total Probability is not slightly less than 1");



        final Set<SymbolProb> symbolProbSet4 = new Reel.SymbolMapBuilder()
                                                            .put(symbol1, 0.1)
                                                            .put(symbol2, 0.2)
                                                            .put(symbol3, 0.699999999)
                                                            .put(symbol4, 0.000000001)
                                                            .put(symbol5, 0.000001)
                                                            .build();
        assertThrows(ReelException.class,
                ()-> new Reel(symbolProbSet4),
                "Total Probability exceeded 1 a lot");

    }

    @Test
    void simSizeTest(){


        Symbol symbol1 = symbolService.findSymbolById(1).get();
        Symbol symbol2 = symbolService.findSymbolById(2).get();
        Symbol symbol3 = symbolService.findSymbolById(3).get();

        Set<SymbolProb> symbolProbSet = new Reel.SymbolMapBuilder()
                                                    .put(symbol1, 0.1)
                                                    .put(symbol2, 0.2)
                                                    .put(symbol3, 0.7)
                                                    .build();



        Reel reel = new Reel(symbolProbSet);

        assertEquals(1, reel.simulate(1).size(), "Simulate Outcome Size = 1");
        assertEquals(4, reel.simulate(4).size(), "Simulate Outcome Size = 4");

        assertThrows(
                ReelException.class,
                () -> reel.simulate(0),
                "Cannot simulate with outcome size = 0"
        );

    }




}

