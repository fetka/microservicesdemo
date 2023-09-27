package com.example.addressservice.hystrix;

import com.netflix.hystrix.HystrixCommand;

public class RemoteServiceTestCommand extends HystrixCommand<String> {
    private RemoteServiceTestSimulator testSimulator;

    public RemoteServiceTestCommand(Setter config, RemoteServiceTestSimulator testSimulator) {
        super(config);
        this.testSimulator = testSimulator;
    }

    @Override
    protected String run() throws Exception {
        return testSimulator.execute();
    }
}
