package org.techcr.thread.racecondition;

public class ClassWithCriticalSection {

    public static void main(String[] args) throws InterruptedException {
        ClassWithCriticalSection classWithCriticalSection = new ClassWithCriticalSection();
        classWithCriticalSection.fixWithSynchronized();
        classWithCriticalSection.fixWithLock();
    }

    public void fixWithLock() throws InterruptedException {
        SynchronizedInventoryController synchronizedInventoryController = new SynchronizedInventoryController();
        IncrementingThread incrementingThread = new IncrementingThread(synchronizedInventoryController);
        DecrementingThread decrementingThread = new DecrementingThread(synchronizedInventoryController);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("Balance Items With Lock: " + synchronizedInventoryController.getItems());
    }

    public void fixWithSynchronized() throws InterruptedException {
        SynchronizedInventoryController synchronizedInventoryController = new SynchronizedInventoryController();
        IncrementingThread incrementingThread = new IncrementingThread(synchronizedInventoryController);
        DecrementingThread decrementingThread = new DecrementingThread(synchronizedInventoryController);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("Balance Items With Synchronized: " + synchronizedInventoryController.getItems());
    }

    public static class IncrementingThread extends Thread {
        private InventoryController inventoryController;
        public IncrementingThread(InventoryController inventoryController) {
            this.inventoryController = inventoryController;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                inventoryController.increment();
            }
        }
    }

    public static class DecrementingThread extends Thread {
        private InventoryController inventoryController;
        public DecrementingThread(InventoryController inventoryController) {
            this.inventoryController = inventoryController;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                inventoryController.decrement();
            }
        }
    }

    public interface InventoryController {
        void increment();
        void decrement();
        int getItems();
    }

    public static class SynchronizedInventoryController implements InventoryController{
        private int items = 0;

        public synchronized void increment() {
            items++;
        }

        public synchronized void decrement() {
            items--;
        }

        public synchronized int getItems() {
            return items;
        }
    }

    public static class LockInventoryController implements InventoryController{
        private int items = 0;
        Object lockingObject = new Object();

        public void increment() {
            synchronized (lockingObject) {
                items++;
            }
        }

        public void decrement() {
            synchronized (lockingObject) {
                items--;
            }
        }

        public int getItems() {
            synchronized (lockingObject) {
                return items;
            }
        }
    }
}
