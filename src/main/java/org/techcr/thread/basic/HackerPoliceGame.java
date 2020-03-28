package org.techcr.thread.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HackerPoliceGame {
    public static final int MAX_PASSWORD = 9999;

    public static void main(String[] args) {
        Random random = new Random();
        Vault vault = new Vault(random.nextInt(MAX_PASSWORD));
        List<Thread> threads = new ArrayList<>(1);
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new PoliceThread());
        threads.forEach(Thread::start);
    }
    private static class Vault {
        private int password;

        public Vault(int password) {
            this.password = password;
        }

        public boolean isCorrectPassword(int guess)  {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.password == guess;
        }
    }

    public static abstract class AbstractHackerThread extends Thread {
        protected Vault vault;

        public AbstractHackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public synchronized void start() {
            System.out.println("Start Thread: " + this.getName());
            super.start();
        }
    }

    public static class AscendingHackerThread extends AbstractHackerThread {

        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = 0; i < MAX_PASSWORD; i++) {
                if (vault.isCorrectPassword(i)) {
                    System.out.println("Guess is correct: " + this.getName());
                    System.exit(0);
                }

            }
        }
    }

    public static class DescendingHackerThread extends AbstractHackerThread {

        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = MAX_PASSWORD; i > 0; i--) {
                if (vault.isCorrectPassword(i)) {
                    System.out.println("Guess is correct: " + this.getName());
                    System.exit(0);
                }
            }
        }
    }

    public static class PoliceThread extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Hackers loss. ");
            System.exit(0);
        }
    }
}

