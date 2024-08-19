package com.davidklhui.slotgame;

import com.davidklhui.slotgame.model.*;
import com.davidklhui.slotgame.service.IPaylineService;
import com.davidklhui.slotgame.service.IPayoutDefinitionService;
import com.davidklhui.slotgame.service.ISymbolService;
import com.davidklhui.slotgame.service.SlotGameServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@TestPropertySource("/application-dev.yml")
@SpringBootTest(classes = SlotGameApplication.class)
@Transactional
@Slf4j
@SqlGroup({
        @Sql(scripts = "/insert_symbols.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/insert_coordinates.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/insert_paylines.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/insert_payouts.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "/delete_payouts.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/delete_paylines.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/delete_coordinates.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "/delete_symbols.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),

})
class PayoutTest {

    @Mock
    private Slot slotMock;

    @Autowired
    private IPaylineService paylineService;

    @Autowired
    private IPayoutDefinitionService payoutDefinitionService;

    @Autowired
    private ISymbolService symbolService;

    @Autowired
    private SlotGameServiceImpl slotGameService;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;



    @Test
    void matchOnlyFirstPaylineId1Test(){
        int temp = 100;
        Symbol symbol1 = new Symbol(1, "1", false);

        // generate an outcome that only the first row is 1, else are different
        List<List<Symbol>> outcomes = Arrays.asList(
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp, "1"))
        );

        when(slotMock.spin()).thenReturn(outcomes);

        final Optional<Payline> paylineOptional = paylineService.findPaylineById(1);
        assertTrue(paylineOptional.isPresent(), "Payline 1 present");

        final Payline payline = paylineOptional.get();

        final List<Symbol> paylineCoordinateSymbols = ReflectionTestUtils.invokeMethod(slotGameService, "extractOutcomeSymbolByCoordinates",
                                                                                    slotMock.spin(), payline.getCoordinates());
        final List<Symbol> expectedExtractedSymbols = Collections.nCopies(5, symbol1);
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
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp++, "1")),
                Arrays.asList(symbol1, new Symbol(temp++, "1"), new Symbol(temp, "1"))
        );
        when(slotMock.spin()).thenReturn(outcomes);

        Optional<Payline> paylineOptional = paylineService.findPaylineById(1);
        assertTrue(paylineOptional.isPresent(), "Payline 1 exists");

        Payline payline = paylineOptional.get();

        PayoutDefinition payoutDefinition = new PayoutDefinition(
                1,
                payline,
                symbol1,
                30);

        SlotSpinPayout slotSpinPayout = ReflectionTestUtils.invokeMethod(slotGameService, "payoutForSpecificPayline", slotMock.spin(), payoutDefinition);
        int actualPayout = slotSpinPayout.getPayoutAmount();

        assertEquals(30, actualPayout);


        paylineOptional = paylineService.findPaylineById(2);
        assertTrue(paylineOptional.isPresent(), "Payline 2 exists");
        payline = paylineOptional.get();

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
                Arrays.asList(symbol1, symbol2, new Symbol(temp++, "1")),
                Arrays.asList(symbol1, symbol2, new Symbol(temp++, "1")),
                Arrays.asList(symbol1, symbol2, new Symbol(temp, "1"))
        );
        when(slotMock.spin()).thenReturn(outcomes);

        // payline 1 is the first horizontal line
        final Optional<Payline> paylineOptional1 = paylineService.findPaylineById(1);
        assertTrue(paylineOptional1.isPresent());
        final Payline payline1 = paylineOptional1.get();
        final PayoutDefinition payoutDefinition1 = new PayoutDefinition(1, payline1, symbol1, 30);

        // payline 2 is the second horizontal line
        final Optional<Payline> paylineOptional2 = paylineService.findPaylineById(2);
        assertTrue(paylineOptional2.isPresent());
        final Payline payline2 = paylineOptional2.get();
        final PayoutDefinition payoutDefinition2 = new PayoutDefinition(2, payline2, symbol2, 300);

        final List<PayoutDefinition> payoutDefinitions = Arrays.asList(payoutDefinition1, payoutDefinition2);

        final SlotSpinResult slotSpinResult = ReflectionTestUtils.invokeMethod(slotGameService, "slotSpinResult", slotMock.spin(), payoutDefinitions);
        final int actualPayout = slotSpinResult.getSpinPayout();

        assertEquals(330, actualPayout);


    }


    @Test
    void verifySlotSpinPayoutTest2(){

        Symbol symbol1 = new Symbol(1, "1");


        // generate an outcome of all 1
        List<List<Symbol>> outcomes = Arrays.asList(
                Arrays.asList(symbol1, symbol1, symbol1),
                Arrays.asList(symbol1, symbol1, symbol1),
                Arrays.asList(symbol1, symbol1, symbol1),
                Arrays.asList(symbol1, symbol1, symbol1),
                Arrays.asList(symbol1, symbol1, symbol1));
        when(slotMock.spin()).thenReturn(outcomes);

        // payline 1 is the first horizontal line
        final Optional<Payline> paylineOptional1 = paylineService.findPaylineById(1);
        assertTrue(paylineOptional1.isPresent());
        final Payline payline1 = paylineOptional1.get();
        final PayoutDefinition payoutDefinition1 = new PayoutDefinition(1, payline1, symbol1, 30);

        // payline 2 is the second horizontal line
        final Optional<Payline> paylineOptional2 = paylineService.findPaylineById(2);
        assertTrue(paylineOptional1.isPresent());
        final Payline payline2 = paylineOptional2.get();
        final PayoutDefinition payoutDefinition2 = new PayoutDefinition(2, payline2, symbol1, 300);

        // payline 3 is the second horizontal line
        final Optional<Payline> paylineOptional3 = paylineService.findPaylineById(3);
        assertTrue(paylineOptional3.isPresent());
        final Payline payline3 = paylineOptional3.get();
        final PayoutDefinition payoutDefinition3 = new PayoutDefinition(3, payline3, symbol1, 5000);

        final List<PayoutDefinition> payoutDefinitions = Arrays.asList(payoutDefinition1, payoutDefinition2, payoutDefinition3);

        final SlotSpinResult slotSpinResult = ReflectionTestUtils.invokeMethod(slotGameService, "slotSpinResult", slotMock.spin(), payoutDefinitions);
        final int actualPayout = slotSpinResult.getSpinPayout();

        assertEquals(5330, actualPayout);


    }

    @Test
    void assertDefinedPayoutSizeTest(){
        int payoutListSize = payoutDefinitionService.listPayoutDefinitions().size();

        // 11 defined paylines * 5 different regular symbols
        assertEquals(55, payoutListSize, "Correct Payout List Size");

        int symbolListSize = symbolService.listSymbols()
                .stream().filter(symbol-> ! symbol.isWild()).toList().size();

        // 11 * 5 different payouts, times 5 replication (e.g matched symbol 1 across 5 reels)
        int payoutSymbolListSize = payoutListSize * symbolListSize;

        assertEquals(275, payoutSymbolListSize);
    }

    /*
        only use when you need to manually insert data into db
        used to avoid manually write the .sql file
     */
    boolean prepared = false;

//    @BeforeEach
    void setupPayoutSymbols(){

        // find only regular symbols (non-wild)
        int symbolLength = symbolService.listSymbols().stream().filter(symbol-> ! symbol.isWild()).toList().size();
        int numPaylines = paylineService.listPaylines().size();

        // because 5 identical symbols across 5 reels
        int replicate = 5;

        if(! prepared) {

            Map<String, Integer>[] parameters;
            int payoutId = 1;
            int i;

            for(int symbolId=1; symbolId<=symbolLength; symbolId++){
                // symbol ids
                i = 0;
                parameters = new Map[numPaylines * replicate];
                for(int k=0; k < numPaylines; k++) {
                    for (int j = 0; j < replicate; j++) {
                        Map<String, Integer> parameter = new HashMap<>();
                        parameter.put("payoutId", payoutId);
                        parameter.put("symbolId", symbolId);
                        parameters[i] = parameter;
                        i++;
                    }
                    payoutId++;
                }
                jdbcTemplate.batchUpdate(
                        "INSERT INTO payout_symbols VALUES (:payoutId, :symbolId)",
                        parameters
                );
            }
            prepared = true;
        }
    }
}
