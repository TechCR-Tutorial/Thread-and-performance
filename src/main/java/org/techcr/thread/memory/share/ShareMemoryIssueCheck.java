package org.techcr.thread.memory.share;

public class ShareMemoryIssueCheck {

    public static void main(String[] args) throws InterruptedException {
        InventoryController inventoryController = new InventoryController();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryController);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryController);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("Balance Items: " + inventoryController.getItems());
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

    public static class InventoryController {
        private int items = 0;

        public void increment() {
            items++;
        }

        public void decrement() {
            items--;
        }

        public int getItems() {
            return items;
        }
    }
}
