package org.techcr.thread.racecondition.atomic.operations;

import java.util.Random;

public class AtomicOperationCheck {

    public static void main(String[] args) {
        Metrics metrics = new Metrics();
        BusinessLogic businessLogicThread1 = new BusinessLogic(metrics);
        BusinessLogic businessLogicThread2 = new BusinessLogic(metrics);

        MetricsPrinter printer = new MetricsPrinter(metrics);

        businessLogicThread1.start();
        businessLogicThread2.start();
        printer.start();

    }
    public static class MetricsPrinter extends Thread {
        private Metrics metrics;

        public MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("interrupted Exception MetricsPrinter:");
                }
                System.out.println("Current average is: " + metrics.getAverage());
            }
        }
    }
    public static class BusinessLogic extends Thread {
        private Metrics metrics;
        private Random random = new Random();

        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while(true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    System.out.println("interrupted Exception BusinessLogic:");
                }
                long end = System.currentTimeMillis();
                metrics.addSample(end - start);
            }
        }
    }

    public static class Metrics {
        private long count = 0;
        //make average volatile since double is not atomic
        private volatile double average = 0.0;

        //make this addSample synchronized this is not atomic operation.
        public synchronized void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = ( currentSum + sample ) / count;
        }

        public double getAverage() {
            return average;
        }
    }
}
