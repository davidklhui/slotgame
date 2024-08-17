package com.davidklhui.slotgame.model;

import com.davidklhui.slotgame.exception.PayoutException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
    This class defines how to perform deserialization for payout json object
    note for two special fields:
    1. payline: it is a specific class involved dependency injection
    2. symbols: from the JSON object it is also acceptable to provide List<Symbol> or simple Symbol,
                so determine separately
 */
@Component
public class PayoutDefinitionDeserializer extends JsonDeserializer<PayoutDefinition> {

    @Override
    public PayoutDefinition deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        final ObjectNode node = jp.getCodec().readTree(jp);

        final int payoutId = node.get("payoutId").asInt();
        final int payoutAmount = node.get("payoutAmount").asInt();
        final Payline payline;

        /*
            check if the json node contains "payline"
            if yes then perform deserialization for the payline
            if no directly throws exception
         */
        if (node.has("payline")) {

            final JsonNode paylineNode = node.get("payline");
            payline = jp.getCodec().treeToValue(paylineNode, Payline.class);
        } else {
            throw new PayoutException("Missing Payline definition");
        }

        /*
            the node must contain either symbol (single symbol json object) or symbols (list of symbol json object)
            if not find either then throws exception
         */
        if(node.has("symbols")){
            final JsonNode symbolsNode = node.get("symbols");

            if (symbolsNode.isArray()) {
                final List<Symbol> symbols = new ArrayList<>();

                for (JsonNode symbolNode : symbolsNode) {
                    final Symbol symbol = jp.getCodec().treeToValue(symbolNode, Symbol.class);
                    symbols.add(symbol);
                }
                return new PayoutDefinition(payoutId, payline, symbols, payoutAmount);
            }
        } else if(node.has("symbol")){

            final JsonNode symbolNode = node.get("symbol");
            final Symbol symbol = jp.getCodec().treeToValue(symbolNode, Symbol.class);

            return new PayoutDefinition(payoutId, payline, symbol, payoutAmount);
        }

        throw new PayoutException("Payout deserialization missing specific fields or has incorrect structure");
    }
}
