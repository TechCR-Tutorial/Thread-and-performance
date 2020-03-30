package org.techcr.thread.racecondition.atomic.operations;

public class RaceConditionCheck {

    public static void main(String[] args) {
        RaceConditionCheck raceConditionCheck = new RaceConditionCheck();
//        System.out.println("Check Race condition issue");
//        raceConditionChecktionCheck.checkRaceConditionIssue();
        System.out.println("Check Race condition fix");
        raceConditionCheck.checkRaceConditionFixed();
    }

    void checkRaceConditionFixed() {
        SynchronizedSharedResource sharedResource = new SynchronizedSharedResource();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedResource.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedResource.checkForDataRace();
            }
        });
        thread1.start();
        thread2.start();
    }
    /**
     * Run this and you can see data race detected.
     * according to order of execution y > x cannot be happen. {@link SharedResource#checkForDataRace()}
     */
    public void checkRaceConditionIssue() {
        SharedResource sharedResource = new SharedResource();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedResource.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedResource.checkForDataRace();
            }
        });
        thread1.start();
        thread2.start();
    }
    public static class SharedResource {
        private int x = 0;
        private int y = 0;
        public void increment() {
            x++;
            y++;
        }
        //according to order of execution y > x cannot be happen.
        public void checkForDataRace() {
            if ( y > x) {
                System.out.println(" y > x data race is detected: x:y=" + x + ":" + y);
            }
        }
    }
    /*
    Added volatile key word.
     */
    public static class SynchronizedSharedResource {
        private volatile int x = 0;
        private volatile int y = 0;
        public void increment() {
            x++;
            y++;
        }
        //according to order of execution y > x cannot be happen.
        public void checkForDataRace() {
            if ( y > x) {
                System.out.println(" y > x data race is detected: x:y=" + x + ":" + y);
            }
        }
    }
}
