package com.davidklhui.slotgame;

import com.davidklhui.slotgame.exception.SymbolException;
import com.davidklhui.slotgame.model.Symbol;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SlotGameApplication.class)
class SymbolTest {

    @Test
    void symbolValidConfigTest(){

        // create a symbol instance for testing
        assertDoesNotThrow(()-> new Symbol(1, "symbol_1", BigDecimal.valueOf(0.1), true)
                , "Symbol Probability is between 0 and 1");


        Symbol symbol = new Symbol(1, "symbol_1", BigDecimal.valueOf(0.1), true);

        // symbol not null
        assertNotNull(symbol, "Symbol is not null");

        // symbol id is 1
        assertEquals( 1, symbol.getSymbolId(),"Symbol id is correct");

        // symbol has name symbol_1
        assertEquals("symbol_1", symbol.getSymbolName(), "Symbol name is correct");


    }

    // test valid probability: between 0 & 1 inclusively
    @Test
    void symbolValidProbabilityTest(){

        // symbol probability is valid
        assertDoesNotThrow(()-> new Symbol(1, "symbol_1", BigDecimal.valueOf(0.5), true),
                "Symbol Probability is between 0 and 1");

        assertDoesNotThrow(()-> new Symbol(2, "symbol_2", BigDecimal.valueOf(0.0), true),
                "Symbol Probability is between 0 and 1");

        assertDoesNotThrow(()-> new Symbol(3, "symbol_3", BigDecimal.valueOf(1.0), true),
                "Symbol Probability is between 0 and 1");

    }

    // test invalid probability: < 0 or > 1:
    @Test
    void symbolInvalidProbabilityTest(){

        // symbol is invalid case: <0
        assertThrows(SymbolException.class,
                ()-> new Symbol(1, "symbol_1", -0.01, true),
                "Symbol Probability less than 0");

        // symbol is invalid case: > 1
        assertThrows(SymbolException.class,
                ()-> new Symbol(2, "symbol_2", 1.00001, true),
                "Symbol Probability greater than 1");

    }



    // test valid name
    @Test
    void symbolValidNameTest(){

        // symbol valid name
        assertDoesNotThrow(
                ()-> new Symbol(1, "symbol_1", BigDecimal.valueOf(0.01), true),
                "Symbol Probability less than 0");

    }

    // test valid name
    @Test
    void symbolInvalidNameTest(){


        // symbol name is null
        assertThrows(
                SymbolException.class,
                ()-> new Symbol(1, null, 0.01, true),
                "Symbol Name is null");

        // symbol name is an empty string
        assertThrows(
                SymbolException.class,
                ()-> new Symbol(1, "", 0.01, true),
                "Symbol Name is empty string");

        // symbol name only contains space
        assertThrows(
                SymbolException.class,
                ()-> new Symbol(1, "    ", 0.01, true),
                "Symbol Name only contains space");

    }


    @Test
    void equalSymbolTest(){

        Symbol symbol1 =  new Symbol(1, "s1", new BigDecimal(0.01), true);
        Symbol symbol2 =  new Symbol(1, "s1", new BigDecimal(0.02), true);
        Symbol symbol3 =  new Symbol(1, "s2", new BigDecimal(0.03), true);
        Symbol symbol4 =  new Symbol(2, "s1", new BigDecimal(0.04), true);

        // same id is equal regardless of name
        assertEquals(symbol1, symbol2, "Same Symbol id");
        assertEquals(symbol1, symbol3, "Same Symbol id even have different name");

        // different id is not equal
        assertNotEquals(symbol1, symbol4, "Different symbol id even have the same name");


    }

}
