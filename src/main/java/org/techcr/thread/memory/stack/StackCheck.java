package org.techcr.thread.memory.stack;

public class StackCheck {

    public static void main(String[] args) {
        int x = 1;
        int y = 2;
        int result = sum(x, y);
        System.out.println("Result: " + result);
    }

    private static int sum(int a, int b) {
        int s = a + b;
        return s;
    }
}
