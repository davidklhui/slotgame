package com.davidklhui.slotgame;


import com.davidklhui.slotgame.model.Coordinate;
import com.davidklhui.slotgame.repository.CoordinateRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-dev.yml")
@SpringBootTest(classes = SlotGameApplication.class)
@Transactional
@Slf4j
@SqlGroup({
        @Sql(scripts = "/insert_coordinates.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(scripts = "/delete_coordinates.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
})
class CoordinateTest {

    /*
    Involved test:
    1. check if the list size is the same as what we've inserted

 */

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Test
    void assertSizeTest(){
        List<Coordinate> coordinateList = coordinateRepository.findAll();

        assertEquals(15, coordinateList.size());

    }
}
