package com.example.addressservice.hystrix;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class CommandHelloWorldTest {

    @Test
    public void givenInputBobAndDefaultSettings_whenCommandExecuted_thenReturnHelloBob(){
        assertThat(new CommandHelloWorld("Bob").execute(), equalTo("Hello Bob and Bobek!"));
    }

}