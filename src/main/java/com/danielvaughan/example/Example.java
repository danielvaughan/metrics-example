package com.danielvaughan.example;

import com.codahale.metrics.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

public class Example {

    private static final MetricRegistry metrics = new MetricRegistry();

    private static final Random random = new Random();

    public static void main(String args[]) {
        Example example = new Example();
        example.run();
    }

    private void run() {
        startReport();
        createTimer();
        createCounter();
        createMeter();
        createGauge();
        createHistogram();
        wait5Seconds();
    }

    private void createGauge() {
        metrics.register(MetricRegistry.name(Example.class, "test-gauge"),
                new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        return random.nextInt(); //gauge a random number
                    }
                });
    }

    private void createHistogram() {
        Histogram histogram = metrics.histogram(name(Example.class, "test-histogram"));
        for (int i = 0; i < 10; i++) {
            histogram.update(random.nextInt()); //update the histogram with a random number
        }
    }

    private void createMeter() {
        Meter meter = metrics.meter("example-meter");
        meter.mark(); //record the event has occured
    }

    private void createCounter() {
        Counter counter = metrics.counter(name(Example.class, "example-counter"));
        counter.inc(); //increment the counter
    }

    private void createTimer() {
        Timer timer = metrics.timer(name(Example.class, "example-timer"));
        Timer.Context context = timer.time(); //start the timer
        for (int i = 0; i<10000; i++) // The code you want to time
        {
            Math.sqrt(i);
        }
        context.stop(); //stop the timer
    }

    private void startReport() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    private void wait5Seconds() {
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
        }
    }
}