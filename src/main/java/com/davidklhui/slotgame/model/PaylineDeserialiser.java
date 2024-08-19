package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.PaylineException;
import com.davidklhui.slotgame.service.IPaylineService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PaylineDeserialiser extends JsonDeserializer<Payline> {

    private final IPaylineService paylineService;

    @Autowired
    public PaylineDeserialiser(final IPaylineService paylineService){
        this.paylineService = paylineService;
    }

    @Override
    public Payline deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {

        ObjectNode node = jp.getCodec().readTree(jp);

        final int id = node.get("paylineId").asInt();

        return paylineService.findPaylineById(id)
                .orElseThrow(()->new PaylineException(String.format("Payline not found, id=%d", id)));
    }
}
