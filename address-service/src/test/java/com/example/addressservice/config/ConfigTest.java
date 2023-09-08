package com.example.addressservice.config;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootTest
public class ConfigTest {

    @Autowired
    Environment environment;

    @BeforeAll()
    public static void before(){
//        Environment environment1= env
    }

    @Test
    public void test(){
        System.out.println("********************************************* ");
        try {

            System.out.println(environment.getActiveProfiles()[0].toString());
        }catch (Exception e){

        }
//        System.out.println("test");
        System.out.println(" *********************************************");
    }
}
