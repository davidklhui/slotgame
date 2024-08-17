package com.davidklhui.slotgame.config;

import com.davidklhui.slotgame.model.Payline;
import com.davidklhui.slotgame.model.PaylineDeserialiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /*
        Declare how to perform deserialization for the field Payline
        this handling comes from the fact that the PaylineDeserialiser required dependency injection
     */
    @Bean
    public ObjectMapper objectMapper(PaylineDeserialiser paylineDeserialiser) {
        final ObjectMapper mapper = new ObjectMapper();

        // Register your custom deserializer
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(Payline.class, paylineDeserialiser);
        mapper.registerModule(module);

        return mapper;
    }


}
