package com.example.addressservice.hystrix;

public class RemoteServiceTestSimulator {
    private long wait;

    public RemoteServiceTestSimulator(long wait) throws InterruptedException {
        this.wait = wait;
    }

    String execute() throws InterruptedException {
        Thread.sleep(wait);
        return "Success";
    }
}
