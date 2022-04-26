
public class ArrayDequeTest {
    public static boolean checkBool(boolean expected, boolean actual) {
        if (expected != actual) {
            System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static boolean checkInt(int expected, int actual) {
        if (expected != actual) {
            System.out.println("size() returned " + actual + ", but expected: " + expected);
            return false;
        }
        return true;
    }

    public static void printTestStatus(boolean passed) {
        if (passed) {
            System.out.println("Test passed!\n");
        } else {
            System.out.println("Test failed!\n");
        }
    }

    public static void addRemoveLastTest(){
        ArrayDeque<Integer> dq = new ArrayDeque<Integer>();
        boolean pass = checkInt(0, dq.size());
        pass = checkBool(true, dq.isEmpty()) && pass;
        dq.addLast(3);
        dq.addLast(2);
        dq.addLast(1);
        pass = checkInt(3, dq.size()) && pass;
        pass = checkInt(1, dq.get(2)) && pass;
        pass = checkInt(3, dq.get(0)) && pass;
        Integer n = dq.removeLast();
        pass = checkInt(2, dq.size()) && pass;
        pass = checkInt(1, n) && pass;
        n = dq.removeLast();
        n = dq.removeLast();
        pass = checkInt(0, dq.size()) && pass;
        pass = checkInt(3, n) && pass;
        printTestStatus(pass);
        return;
    }

    public static void addRemoveFirstTest(){
        ArrayDeque<Integer> dq = new ArrayDeque<Integer>();
        boolean pass = true;
        dq.addFirst(3);
        dq.addFirst(2);
        dq.addFirst(1);
        pass = checkInt(3, dq.size()) && pass;
        pass = checkInt(3, dq.get(2)) && pass;
        pass = checkInt(1, dq.get(0)) && pass;
        Integer n = dq.removeFirst();
        pass = checkInt(2, dq.size()) && pass;
        pass = checkInt(1, n) && pass;
        n = dq.removeFirst();
        n = dq.removeFirst();
        pass = checkInt(0, dq.size()) && pass;
        pass = checkInt(3, n) && pass;
        printTestStatus(pass);
        return;
    }

    public static void addRemoveFuseTest(){}

    public static void main(String[] args) {
        System.out.println("Running tests.\n");
        addRemoveLastTest();
        addRemoveFirstTest();
    }
}
