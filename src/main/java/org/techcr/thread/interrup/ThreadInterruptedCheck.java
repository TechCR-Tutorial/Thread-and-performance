package org.techcr.thread.interrup;

import java.math.BigInteger;

public class ThreadInterruptedCheck {

    public static void main(String[] args) {
        /**
         * This thread handling interrupted exception.
         * so once thread get interrupted, thread it self interrupted and dead.
         * Handle inside: {@link BlockingTask#run()}
         */
        Thread blockingThread = new Thread(new BlockingTask());
        blockingThread.start();
        blockingThread.interrupt();

        /**
         This tread dose not handle interrupted exception. in this case thread it self need to check the thread
         is interrupted.
         Ex: {@link LongComputationTask#getPower} Remove interrupted check and test, thread will not stop.
         */
        BigInteger base = new BigInteger("2000000");
        BigInteger power = new BigInteger("1000000");
        System.out.println("Finding power....");
        Thread longComputationThread = new Thread(new LongComputationTask(base, power));
        longComputationThread.start();
        longComputationThread.interrupt(); // this interrupted method call is not enough. need to check it inside thread

        /**
         * Here {@link LongComputationDaemonTask} getPower method not handling interrupted signal.
         * but still, since we make this thread as daemon thread it wont block our main application exit.
         */
        BigInteger daemonBase = new BigInteger("2000000");
        BigInteger daePower = new BigInteger("1000000");
        System.out.println("Finding power....");
        Thread longComputationDaemonThread = new Thread(new LongComputationDaemonTask(daemonBase, daePower));
        longComputationDaemonThread.setDaemon(true);
        longComputationDaemonThread.start();
        longComputationDaemonThread.interrupt();
    }

    public static class BlockingTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(500000);
            } catch (InterruptedException e) {
                System.out.println("Exiting blocking thread");
            }
        }
    }

    public static class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println("LongComputationTask Power values is: " + getPower());
        }

        private BigInteger getPower() {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("LongComputationTask thread interrupted..");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }

    public static class LongComputationDaemonTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationDaemonTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println("LongComputationDaemonTask Power values is: " + getPower());
        }

        private BigInteger getPower() {
            BigInteger result = BigInteger.ONE;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                result = result.multiply(base);
            }
            return result;
        }
    }
}


