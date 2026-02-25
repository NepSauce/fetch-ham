package ham.hamcrawler.engine;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class HamBugLogger {
    private static final long LOG_INTERVAL_MS = 62;
    private static final BlockingQueue<String> QUEUE = new LinkedBlockingQueue<>();

    static {
        Thread loggerThread = new Thread(() -> {
            while (true) {
                try {
                    String message = QUEUE.take();
                    System.out.println(message);
                    Thread.sleep(LOG_INTERVAL_MS);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }, "hambug-logger");
        loggerThread.setDaemon(true);
        loggerThread.start();
    }

    private HamBugLogger() {
    }

    public static void log(String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        QUEUE.offer(message);
    }
}
