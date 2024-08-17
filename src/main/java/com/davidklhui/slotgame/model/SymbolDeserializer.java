package com.davidklhui.slotgame.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class SymbolDeserializer extends JsonDeserializer<Symbol> {
    @Override
    public Symbol deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        ObjectNode node = jp.getCodec().readTree(jp);

        final int id = node.get("id").asInt();
        final String name;
        final boolean isWild;

        // id is mandatory, rest may be ignored from the rest api
        if(node.has("name")) {
            name = node.get("name").asText();
        } else {
            name = String.valueOf(id);
        }

        if(node.has("isWild")) {
            isWild = node.get("isWild").asBoolean();
        } else {
            isWild = false;
        }

        if(node.has("probability")) {
            final double probability = node.get("probability").asDouble();
            return new Symbol(id, name, probability, isWild);
        } else {
            return new Symbol(id, name, isWild);
        }

    }
}
