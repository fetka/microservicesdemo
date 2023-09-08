package com.example.addressservice.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AddressConfig {
    @Autowired
    Environment environment;

    @Bean
    public ModelMapper modelMapperBean() {
        var v = environment.getProperty("test.text");
        System.out.print("******************** " + v);
        System.out.println("********************");
        return new ModelMapper();
    }

}
