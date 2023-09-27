package com.example.addressservice.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.jupiter.api.Test;
import rx.Observable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/*
        https://www.baeldung.com/introduction-to-hystrix
*/
class RemoteServiceTestCommandTest {
    @Test
    public void givenSvcTimeoutOf100AndDefaultSettings_whenRemoteSvcExecuted_thenReturnSuccess()
            throws InterruptedException {
        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroup2"));

        assertThat(
                new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(100)).execute(),
                equalTo("Success"));
    }

    @Test
    public void givenSvcTimeoutOf5000AndExecTimeoutOf10000_whenRemoteSvcExecuted_thenReturnSuccess()
            throws InterruptedException {

        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest4"));

        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(10_000);

        config.andCommandPropertiesDefaults(commandProperties);

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(5000)).execute(),
                equalTo("Success"));
    }

    //    HystrixRuntimeException.class
    @Test
    public void givenSvcTimeoutOf12000AndExecTimeoutOf5000_whenRemoteSvcExecuted_thenExpectHre() {
        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest5"));

        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(5_000);
        config.andCommandPropertiesDefaults(commandProperties);
        try {
            RemoteServiceTestSimulator testSimulator = new RemoteServiceTestSimulator(12_000);
           String string = new RemoteServiceTestCommand(config, testSimulator).execute();
            System.out.println("============================");
            System.out.println(string);
            System.out.println("============================");
        } catch (Exception ie) {
            System.out.println(ie);
        }

    }

    @Test
    public void givenSvcTimeoutOf500AndExecTimeoutOf10000AndThreadPool_whenRemoteSvcExecuted_thenReturnSuccess() throws InterruptedException {

        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupThreadPool"));

        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(10_000);
        config.andCommandPropertiesDefaults(commandProperties);
        config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                .withMaxQueueSize(10)
                .withCoreSize(3)
                .withQueueSizeRejectionThreshold(10));

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
                equalTo("Success"));
    }

    @Test
    public void givenCircuitBreakerSetup_whenRemoteSvcCmdExecuted_thenReturnSuccess()
            throws InterruptedException {

        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));

        HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
        properties.withExecutionTimeoutInMilliseconds(1000);
        properties.withCircuitBreakerSleepWindowInMilliseconds(4000);
        properties.withExecutionIsolationStrategy
                (HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
        properties.withCircuitBreakerEnabled(true);
        properties.withCircuitBreakerRequestVolumeThreshold(1);

        config.andCommandPropertiesDefaults(properties);
        config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                .withMaxQueueSize(1)
                .withCoreSize(1)
                .withQueueSizeRejectionThreshold(1));

        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));

        Thread.sleep(5000);

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
                equalTo("Success"));

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
                equalTo("Success"));

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
                equalTo("Success"));
    }

    public String invokeRemoteService(HystrixCommand.Setter config, int timeout)
            throws InterruptedException {

        String response = null;

        try {
            response = new RemoteServiceTestCommand(config,
                    new RemoteServiceTestSimulator(timeout)).execute();
        } catch (HystrixRuntimeException ex) {
            System.out.println("ex = " + ex);
        }

        return response;
    }
}