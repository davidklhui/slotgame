package com.davidklhui.slotgame;

import com.davidklhui.slotgame.exception.PaylineException;
import com.davidklhui.slotgame.model.Payline;
import com.davidklhui.slotgame.model.PaylineCoordinate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SlotGameApplication.class)
class PaylineTest {

    @Test
    void paylineCoordinateTest(){

        assertThrows(
                PaylineException.class, ()-> PaylineCoordinate.of(1,-1),
                "Coordinate cannot have negative"
        );

        assertThrows(
                PaylineException.class, ()-> PaylineCoordinate.of(-1,2),
                "Coordinate cannot have negative"
        );

        assertThrows(
                PaylineException.class, ()-> PaylineCoordinate.of(-3,-3),
                "Coordinate cannot have negative"
        );

        assertDoesNotThrow(
                ()-> PaylineCoordinate.of(0,0),
                "Coordinate are non negative, accept (0,0)"
        );

        assertDoesNotThrow(
                ()-> PaylineCoordinate.of(0,4),
                "Coordinate are non negative"
        );

        assertDoesNotThrow(
                ()-> PaylineCoordinate.of(5,0),
                "Coordinate are non negative"
        );

        assertDoesNotThrow(
                ()-> PaylineCoordinate.of(5,4),
                "Coordinate are non negative"
        );
    }

    @Test
    void checkIfTwoCoordinatesAreEqualTest(){

        PaylineCoordinate coordinate1 = PaylineCoordinate.of(1,2);
        PaylineCoordinate coordinate2 = PaylineCoordinate.of(1,2);
        PaylineCoordinate coordinate3 = PaylineCoordinate.of(2,2);

        assertEquals(coordinate1, coordinate2, "Coordinates are equal");
        assertNotEquals(coordinate1, coordinate3, "Coordinates are not equal");
    }


    @Test
    void validPaylineTest(){

        List<PaylineCoordinate> paylineCoordinates = List.of(
                PaylineCoordinate.of(0, 0),
                PaylineCoordinate.of(1, 0),
                PaylineCoordinate.of(2, 0),
                PaylineCoordinate.of(3, 0),
                PaylineCoordinate.of(4, 0)
        );

        assertDoesNotThrow(
                ()-> new Payline(1, "horizontal line 0", paylineCoordinates),
                "Not throw exception, no duplication"
        );

        Payline payline = new Payline(1, "horizontal line 0", paylineCoordinates);

        assertEquals(5, payline.getPaylineCoordinates().size(), "Correct size");

    }


    @Test
    void invalidPaylineDuplicatedCoordinatesTest(){

        List<PaylineCoordinate> paylineCoordinates = List.of(
                PaylineCoordinate.of(0, 0),
                PaylineCoordinate.of(1, 0),
                PaylineCoordinate.of(2, 0),
                PaylineCoordinate.of(1, 0),
                PaylineCoordinate.of(4, 0)
        );

        assertThrows(
                PaylineException.class,
                ()-> new Payline(1, "horizontal line 0", paylineCoordinates),
                "Throw exception because coordinate duplicated (1,0) occurred twice"
        );

    }




}
