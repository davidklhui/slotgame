package com.davidklhui.slotgame.service;

import com.davidklhui.slotgame.exception.PaylineException;
import com.davidklhui.slotgame.model.Payline;
import com.davidklhui.slotgame.model.PaylineCoordinate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaylineServiceImpl implements IPaylineService {

    // this part will inject the PaylineRepostory dependency after refactoring
    // current keep this first
//    @Autowired
//    private PaylineRepository paylineRepository;

    @Override
    public List<Payline> listPaylines(){
        final Payline payline1 = new Payline(
                1, "horizontal line 1",
                IntStream.range(0, 3).boxed()
                        .map(i-> PaylineCoordinate.of(i, 0))
                        .toList());

        final Payline payline2 = new Payline(
                2, "horizontal line 2",
                IntStream.range(0, 3).boxed()
                        .map(i-> PaylineCoordinate.of(i, 1))
                        .toList());

        final Payline payline3 = new Payline(
                3, "v shape",
                Arrays.asList(
                        PaylineCoordinate.of(0, 0), PaylineCoordinate.of(1, 1), PaylineCoordinate.of(2, 0)
                ));

        final Payline payline4 = new Payline(
                4, "inverted v shape",
                Arrays.asList(
                        PaylineCoordinate.of(0, 1), PaylineCoordinate.of(1, 0), PaylineCoordinate.of(2, 1)
                ));

        return Arrays.asList(payline1, payline2, payline3, payline4);
    }

    @Override
    public Payline findPaylineById(final int id){
        return listPaylines().stream()
                .filter(payline-> payline.getPaylineId() == id)
                .findFirst()
                .orElseThrow(
                        ()-> new PaylineException(String.format("Payline id not found. Given id: %d", id))
                );

    }
}
