package lab4;
import java.util.ArrayList;
import java.util.List;

public class Lab4 implements Runnable {
    private volatile boolean running = false;
    private Thread worker;

    public void start() {
        if (running) return;
        running = true;
        worker = new Thread(this, "RandomAlphabetPrinter-Thread");
        worker.start();
    }

    public void stop() {
        running = false;
        if (worker != null) worker.interrupt();
    }

    @Override
    public void run() {
        List<Character> letters = new ArrayList<>(26);
        for (char c = 'A'; c <= 'Z'; c++) letters.add(c);

        System.out.println("Starting to print 26 random letters (A-Z) with fluctuating delays...");

        while (running && !letters.isEmpty()) {
            int idx = (int) (Math.random() * letters.size());
            char ch = letters.remove(idx);

            System.out.print(ch + " ");
            System.out.flush();

            int sleepMs = 50 + (int) (Math.random() * 450);
            try {
                Thread.sleep(sleepMs);
            } catch (InterruptedException ie) {
                if (!running) break;
            }
        }

        System.out.println();
        if (letters.isEmpty()) System.out.println("Done: All 26 letters printed.");
        else System.out.println("Stopped before completion. " + letters.size() + " letters remaining.");

        running = false;
    }

    public void join() {
        if (worker == null) return;
        try {
            worker.join();
        } catch (InterruptedException ignored) {}
    }

    public static void main(String[] args) {
        Lab4 pap = new Lab4();
        pap.start();
        pap.join();
    }
}
