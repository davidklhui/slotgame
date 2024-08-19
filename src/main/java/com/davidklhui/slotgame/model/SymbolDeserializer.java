package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.SymbolException;
import com.davidklhui.slotgame.service.ISymbolService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SymbolDeserializer extends JsonDeserializer<Symbol> {

    private final ISymbolService symbolService;

    @Autowired
    public SymbolDeserializer(final ISymbolService symbolService){
        this.symbolService = symbolService;
    }

    @Override
    public Symbol deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        ObjectNode node = jp.getCodec().readTree(jp);

        final int symbolId = node.get("symbolId").asInt();

        return symbolService.findSymbolById(symbolId)
                .orElseThrow(()-> new SymbolException(
                        String.format("Symbol not found, id=%d", symbolId)
                ));
    }
}
