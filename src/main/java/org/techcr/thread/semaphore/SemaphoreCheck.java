package org.techcr.thread.semaphore;

import java.util.concurrent.Semaphore;


public class SemaphoreCheck {

    public static void main(String args[]) throws InterruptedException {
        // creating buffer queue
        Queue queue = new Queue();
        executeThread(new Producer(queue), "Producer-1");
        executeThread(new Producer(queue), "Producer-2");
        executeThread(new Producer(queue), "Producer-3");
        executeThread(new Consumer(queue), "Consumer-1");
        executeThread(new Consumer(queue), "Consumer-2");

        Thread.sleep(50);
        System.out.println("Execute Completed....");

    }

    public static void executeThread(Runnable runnable, String name) {
        Thread thread = new Thread(runnable);
        thread.setName(name);
        thread.setDaemon(true);
        thread.start();
    }

    public static class Queue {

        int item;

        static Semaphore consumerSemaphore = new Semaphore(2);
        static Semaphore producerSemaphore = new Semaphore(3);

        // to get an item from buffer
        void get() {
            try {
                consumerSemaphore.acquire();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }

            System.out.println("Consumer consumed item : " + item + " - " + Thread.currentThread().getName());
            producerSemaphore.release();
        }

        // to put an item in buffer
        void put(int item) {
            try {
                producerSemaphore.acquire();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }

            // producer producing an item
            this.item = item;

            System.out.println("Producer produced item : " + item + " - " + Thread.currentThread().getName());
            consumerSemaphore.release();
        }
    }

    public static class Producer implements Runnable {

        Queue queue;
        int i;
        Producer(Queue queue) {
            this.queue = queue;
        }

        public void run() {
            while (true){
                queue.put(i++);
            }
        }
    }

    // Consumer class
    public static class Consumer implements Runnable {

        Queue queue;

        Consumer(Queue queue) {
            this.queue = queue;
        }

        public void run() {
            while (true) {
                queue.get();
            }
        }
    }

}
