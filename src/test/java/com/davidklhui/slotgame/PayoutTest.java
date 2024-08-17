package com.davidklhui.slotgame;

import com.davidklhui.slotgame.model.*;
import com.davidklhui.slotgame.service.IPaylineService;
import com.davidklhui.slotgame.service.SlotGameServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(classes = SlotGameApplication.class)
class PayoutTest {

    @Mock
    private Slot slotMock;

    @Autowired
    private IPaylineService paylineService;

    @Autowired
    private SlotGameServiceImpl slotGameService;


    @Test
    void matchOnlyFirstPaylineId1Test(){
        int temp = 100;
        Symbol symbol1 = new Symbol(1, "1");

        // generate an outcome that only the first row is 1, else are different
        List<List<Symbol>> outcomes = Arrays.asList(
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp, "1"))
        );

        when(slotMock.spin()).thenReturn(outcomes);

        final Payline payline = paylineService.findPaylineById(1);

        final List<Symbol> paylineCoordinateSymbols = ReflectionTestUtils.invokeMethod(slotGameService, "extractOutcomeSymbolByCoordinates",
                                                                                    slotMock.spin(), payline.getPaylineCoordinates());
        final List<Symbol> expectedExtractedSymbols = Collections.nCopies(3, symbol1);
        assertIterableEquals(
                expectedExtractedSymbols,
                paylineCoordinateSymbols);



    }


    @Test
    void verifyCorrectSlotSpinPayoutSpecificPaylineTest(){

        int temp = 100;
        Symbol symbol1 = new Symbol(1, "1");

        // generate an outcome that only the first row is 1, else are different
        List<List<Symbol>> outcomes = Arrays.asList(
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp, "1"))
        );
        when(slotMock.spin()).thenReturn(outcomes);

        Payline payline = paylineService.findPaylineById(1);

        PayoutDefinition payoutDefinition = new PayoutDefinition(
                1,
                payline,
                symbol1,
                30);

        SlotSpinPayout slotSpinPayout = ReflectionTestUtils.invokeMethod(slotGameService, "payoutForSpecificPayline", slotMock.spin(), payoutDefinition);
        int actualPayout = slotSpinPayout.getPayoutAmount();

        assertEquals(30, actualPayout);


        /*

         */
        payline = paylineService.findPaylineById(2);

        payoutDefinition = new PayoutDefinition(
                2,
                payline,
                symbol1,
                30);

        slotSpinPayout = ReflectionTestUtils.invokeMethod(slotGameService, "payoutForSpecificPayline", slotMock.spin(), payoutDefinition);
        actualPayout = slotSpinPayout.getPayoutAmount();

        assertEquals(0, actualPayout);


    }



    @Test
    void verifySlotSpinPayoutTest(){

        int temp = 100;
        Symbol symbol1 = new Symbol(1, "1");
        Symbol symbol2 = new Symbol(2, "2");

        // generate an outcome that only the first row is 1, second row is 2, else different
        List<List<Symbol>> outcomes = Arrays.asList(
                Arrays.asList(symbol1, symbol2, new Symbol(temp++, "1")),
                Arrays.asList(symbol1, symbol2, new Symbol(temp++, "1")),
                Arrays.asList(symbol1, symbol2, new Symbol(temp, "1"))
        );
        when(slotMock.spin()).thenReturn(outcomes);

        // payline 1 is the first horizontal line
        final Payline payline1 = paylineService.findPaylineById(1);
        final PayoutDefinition payoutDefinition1 = new PayoutDefinition(1, payline1, symbol1, 30);

        // payline 2 is the second horizontal line
        final Payline payline2 = paylineService.findPaylineById(2);
        final PayoutDefinition payoutDefinition2 = new PayoutDefinition(2, payline2, symbol2, 300);

        final List<PayoutDefinition> payoutDefinitions = Arrays.asList(payoutDefinition1, payoutDefinition2);

        final SlotSpinResult slotSpinResult = ReflectionTestUtils.invokeMethod(slotGameService, "slotSpinResult", slotMock.spin(), payoutDefinitions);
        final int actualPayout = slotSpinResult.getSpinPayout();

        assertEquals(330, actualPayout);


    }


    @Test
    void verifySlotSpinPayoutTest2(){

        Symbol symbol1 = new Symbol(1, "1");


        // generate an outcome that only the first row is 1, second row is 2, third row is 3
        List<List<Symbol>> outcomes = Arrays.asList(
                Arrays.asList(symbol1, symbol1),
                Arrays.asList(symbol1, symbol1),
                Arrays.asList(symbol1, symbol1)
        );
        when(slotMock.spin()).thenReturn(outcomes);

        // payline 1 is the first horizontal line
        final Payline payline1 = paylineService.findPaylineById(1);
        final PayoutDefinition payoutDefinition1 = new PayoutDefinition(1, payline1, symbol1, 30);

        // payline 2 is the second horizontal line
        final Payline payline2 = paylineService.findPaylineById(2);
        final PayoutDefinition payoutDefinition2 = new PayoutDefinition(2, payline2, symbol1, 300);

        // payline 3 is the second horizontal line
        final Payline payline3 = paylineService.findPaylineById(3);
        final PayoutDefinition payoutDefinition3 = new PayoutDefinition(3, payline3, symbol1, 5000);

        final List<PayoutDefinition> payoutDefinitions = Arrays.asList(payoutDefinition1, payoutDefinition2, payoutDefinition3);

        final SlotSpinResult slotSpinResult = ReflectionTestUtils.invokeMethod(slotGameService, "slotSpinResult", slotMock.spin(), payoutDefinitions);
        final int actualPayout = slotSpinResult.getSpinPayout();

        assertEquals(5330, actualPayout);


    }


}
