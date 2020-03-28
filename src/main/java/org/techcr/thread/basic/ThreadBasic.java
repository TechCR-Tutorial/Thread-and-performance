package org.techcr.thread.basic;

public class ThreadBasic {

    public static void main(String[] args) throws InterruptedException {
        ThreadBasic basic = new ThreadBasic();

        //Thread creation, set name, set priority.
//        basic.startBasicThread();

        //Unexpected exception handle.
        basic.handleUnexpectedThreadException();

    }

    private void handleUnexpectedThreadException() {
        Thread thread = new Thread(() -> {
            throw new RuntimeException("Throwing exception from thread: " + Thread.currentThread().getName());
        });
        thread.setName("Unexpected-error-thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setUncaughtExceptionHandler((exceptionThread, exception) -> {
            System.out.println("Exception capture from: " + exceptionThread.getName() + "-" + exception.getMessage());
        } );
        thread.start();
    }

    private void startBasicThread() throws InterruptedException {
         /*
        Here you can notice after run Messages print as Message 2, Message 3 and Message 1,4. Even Message 3 print in code
        after execute thread its print before Message 1
         */
        Thread thread = new Thread(() -> {
            System.out.println("Message 1: We are now in thread: " + Thread.currentThread().getName());
            System.out.println("Message 4: Current thread priority is: " + Thread.currentThread().getPriority());
        });

        thread.setName("New Worker Thread.");
        thread.setPriority(Thread.MAX_PRIORITY);

        System.out.println("Message 2: We are in thread: " + Thread.currentThread().getName() + " before start new thread");
        thread.start();
        System.out.println("Message 3: We are in thread: " + Thread.currentThread().getName() + " after start new thread");

        Thread.sleep(5000);
    }
}
