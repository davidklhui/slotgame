package com.davidklhui.slotgame.config;

import com.davidklhui.slotgame.model.Payline;
import com.davidklhui.slotgame.model.PaylineDeserialiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    private final PaylineDeserialiser paylineDeserialiser;

    @Autowired
    public JacksonConfig(final PaylineDeserialiser paylineDeserialiser){
        this.paylineDeserialiser = paylineDeserialiser;
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

        mapper.registerModule(module);

        return mapper;
    }

}
