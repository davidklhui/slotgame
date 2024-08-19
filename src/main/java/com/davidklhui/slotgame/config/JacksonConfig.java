package com.davidklhui.slotgame.config;

import com.davidklhui.slotgame.model.Payline;
import com.davidklhui.slotgame.model.PaylineDeserialiser;

import com.davidklhui.slotgame.model.Symbol;
import com.davidklhui.slotgame.model.SymbolDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    private final PaylineDeserialiser paylineDeserialiser;
    private final SymbolDeserializer symbolDeserializer;

    @Autowired
    public JacksonConfig(final PaylineDeserialiser paylineDeserialiser,
                         final SymbolDeserializer symbolDeserializer){
        this.paylineDeserialiser = paylineDeserialiser;
        this.symbolDeserializer = symbolDeserializer;
    }

    /*
        Declare how to perform deserialization for the field Payline and Symbol
        this handling comes from the fact that some deserializer required dependency injection
     */
    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        // Register custom deserializer
        final SimpleModule module = new SimpleModule();

        module.addDeserializer(Payline.class, paylineDeserialiser);
        module.addDeserializer(Symbol.class, symbolDeserializer);

        mapper.registerModule(module);

        return mapper;
    }

}
