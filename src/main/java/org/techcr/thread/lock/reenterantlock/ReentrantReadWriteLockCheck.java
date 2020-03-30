package org.techcr.thread.lock.reenterantlock;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockCheck {

    public static final int HIGHEST_PRICE = 1000;

    public static void main(String[] args) throws InterruptedException {
        ReentrantReadWriteLockCheck reentrantReadWriteLockCheck = new ReentrantReadWriteLockCheck();
        //Check process with ReentrantLock
        reentrantReadWriteLockCheck.checkForReentrantLock(new ReentrantLockInventoryDatabase(), "ReentrantLock");

        //Check process with ReentrantReadWriteLock
        reentrantReadWriteLockCheck.checkForReentrantLock(new ReentrantReadWriteLockInventoryDatabase(), "ReentrantReadWriteLock");
    }

    public void checkForReentrantLock(InventoryDatabase inventoryDatabase, String lockType)
        throws InterruptedException {
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }
        Thread writeThread = new Thread(() -> {
            while (true) {
                inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
                inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted exception");
                }
            }
        });
        writeThread.setDaemon(true);
        writeThread.start();

        int numberOfThreads = 7;
        List<Thread> readers = new ArrayList<>(1);
        for (int readerIndex = 0; readerIndex < numberOfThreads; readerIndex++) {
            Thread thread = new Thread(() -> {
                for (int i = 0; i < 1000000; i++) {
                    int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getNumberOfItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });
            readers.add(thread);
        }
        long startingTime = System.currentTimeMillis();
        for (Thread reader : readers) {
            reader.start();
        }
        for (Thread reader : readers) {
            reader.join();
        }
        long endingTime = System.currentTimeMillis();
        System.out.println(String.format("%s Reading took %d ms", lockType, endingTime - startingTime));
    }

    public interface InventoryDatabase {
        void addItem(int price);
        void removeItem(int price);
        int getNumberOfItemsInPriceRange(int lowerBound, int upperBound);
    }

    public static class ReentrantReadWriteLockInventoryDatabase implements InventoryDatabase {
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private Lock readLock = lock.readLock();
        private Lock writeLock = lock.writeLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
            readLock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.ceilingKey(upperBound);
                if (fromKey == null || toKey == null) {
                    return 0;
                }
                NavigableMap<Integer, Integer> rangeOfPrice = priceToCountMap.subMap(fromKey, true, toKey, true);
                int sum = 0;
                for (int numberOfItemsPerPrice : rangeOfPrice.values()) {
                    sum += numberOfItemsPerPrice;
                }
                return sum;
            } finally {
                readLock.unlock();
            }
        }

        public void addItem(int price) {
            writeLock.lock();
            try {
                Integer numberOfItemsPerPrice = priceToCountMap.ceilingKey(price);
                priceToCountMap.put(price, (numberOfItemsPerPrice == null ? 0 : numberOfItemsPerPrice) + 1 );
            } finally {
                writeLock.unlock();
            }
        }

        public void removeItem(int price) {
            writeLock.lock();
            try {
                Integer numberOfItemsPerPrice = priceToCountMap.ceilingKey(price);
                if (numberOfItemsPerPrice == null || numberOfItemsPerPrice == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsPerPrice -1 );
                }
            } finally {
                writeLock.unlock();
            }
        }
    }

    public static class ReentrantLockInventoryDatabase implements InventoryDatabase {
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        private ReentrantLock lock = new ReentrantLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
            lock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.ceilingKey(upperBound);
                if (fromKey == null || toKey == null) {
                    return 0;
                }
                NavigableMap<Integer, Integer> rangeOfPrice = priceToCountMap.subMap(fromKey, true, toKey, true);
                int sum = 0;
                for (int numberOfItemsPerPrice : rangeOfPrice.values()) {
                    sum += numberOfItemsPerPrice;
                }
                return sum;
            } finally {
                lock.unlock();
            }
        }

        public void addItem(int price) {
            lock.lock();
            try {
                Integer numberOfItemsPerPrice = priceToCountMap.ceilingKey(price);
                priceToCountMap.put(price, (numberOfItemsPerPrice == null ? 0 : numberOfItemsPerPrice) + 1 );
            } finally {
                lock.unlock();
            }
        }

        public void removeItem(int price) {
            lock.lock();
            try {
                Integer numberOfItemsPerPrice = priceToCountMap.ceilingKey(price);
                if (numberOfItemsPerPrice == null || numberOfItemsPerPrice == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsPerPrice -1 );
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
