package org.techcr.thread.join;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinCheck {

    public static void main(String[] args) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(0L, 23423L, 2355L, 367L, 9784L, 34554L, 29833L);

        /**
         * Remove join clause and run class.
         * you can see that, even main thread stops, still calculation is running.
         *
         * But with join method main thread wait till all calculation is completed.
         */
        List<FactorialThread> factorialThreads = new ArrayList<>(1);
        inputNumbers.forEach(aLong -> factorialThreads.add(new FactorialThread(aLong)));
        for (FactorialThread factorialThread : factorialThreads) {
            factorialThread.start();
        }

        /*
        Remove this join clause to how it behave with out join
         */
        for (FactorialThread factorialThread : factorialThreads) {
            factorialThread.join();
        }

        for (int i = 0; i < inputNumbers.size(); i++) {
            FactorialThread factorialThread = factorialThreads.get(i);
            System.out.println("Check status for number: " + factorialThread.getInputNumber() );
            if (factorialThread.isFinished()) {
                System.out.println("Factorial value for number: " + factorialThread.getInputNumber()
                    + " is: " + factorialThread.getResult());
            } else {
                System.out.println("Factorial calculation for " + factorialThread.getInputNumber() + " is still running");
            }
        }
        System.out.println("Main thread completed. ");
    }

    public static class FactorialThread extends Thread {

        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean finished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.finished = true;
        }

        public BigInteger factorial(long number) {
            System.out.println("Run factorial count for number: " + number);
            BigInteger tempResult = BigInteger.ONE;
            for (long i = number; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            System.out.println("Run completed factorial count for number: " + number);
            return tempResult;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return finished;
        }

        public long getInputNumber() {
            return inputNumber;
        }
    }
}
