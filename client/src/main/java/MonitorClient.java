import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitorClient {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startMonitoring() {
        scheduler.scheduleAtFixedRate(() -> {
            long count = Client.getMissedRequest();

             System.out.println("=====Miss rate====");
             System.out.println("Miss Rate: " + count + " requests in the last minute");
             System.out.println("====================");
             System.out.println();

            // Resetear contadores
            Client.resetMissRequest();

        }, 0, 1, TimeUnit.MINUTES);
    }

    public static void stopMonitoring() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.MINUTES)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
