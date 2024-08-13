package com.davidklhui.slotgame;

import com.davidklhui.slotgame.model.*;
import com.davidklhui.slotgame.service.SlotGameServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(classes = SlotGameApplication.class)
class PayoutTest {

    @Mock
    private Slot slot;

    @Autowired
    private SlotGameServiceImpl slotGameService;

    @Test
    void matchOnlyFirstPaylineId1Test(){
        int temp = 100;
        Symbol symbol1 = new Symbol(1, "1");

        // generate an outcome that only the first row is 1, else are different
        List<List<Symbol>> outcomes = Arrays.asList(
                Arrays.asList(
                        symbol1,
                        new Symbol(temp++, "1"),
                        new Symbol(temp++, "1")),
                Arrays.asList(
                        symbol1,
                        new Symbol(temp++, "1"),
                        new Symbol(temp++, "1")),
                Arrays.asList(
                        symbol1,
                        new Symbol(temp++, "1"),
                        new Symbol(temp++, "1")),
                Arrays.asList(
                        symbol1,
                        new Symbol(temp++, "1"),
                        new Symbol(temp++, "1")),
                Arrays.asList(
                        symbol1,
                        new Symbol(temp++, "1"),
                        new Symbol(temp, "1"))
        );

        when(slot.spin()).thenReturn(outcomes);

        List<PayoutDefinition> payoutDefinitions = new ArrayList<>();
        List<PaylineCoordinate> paylineCoordinates = List.of(
                PaylineCoordinate.of(0, 0),
                PaylineCoordinate.of(1, 0),
                PaylineCoordinate.of(2, 0),
                PaylineCoordinate.of(3, 0),
                PaylineCoordinate.of(4, 0)
        );
        Payline payline = new Payline(1, paylineCoordinates);

        payoutDefinitions.add(
                new PayoutDefinition(
                        1,
                        payline,
                        Collections.nCopies(5, symbol1),
                        30)
        );

        List<Symbol> paylineCoordinateSymbols = ReflectionTestUtils.invokeMethod(slotGameService, "extractOutcomeSymbolByCoordinates", slot.spin(), paylineCoordinates);
        assertIterableEquals(
                List.of(symbol1, symbol1, symbol1, symbol1, symbol1),
                paylineCoordinateSymbols);

        log.info("outcome: {}", slot.spin());
        log.info("payline symbols: {}", paylineCoordinateSymbols);
    }




}
