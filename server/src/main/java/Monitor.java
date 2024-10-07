import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Monitor {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startMonitoring() {
        scheduler.scheduleAtFixedRate(() -> {
            long count = PrinterI.getRequestCount();
            List<Long> jitterList = PrinterI.getJitterList();
            long failedRequests = PrinterI.getRequestNotProcess();
            long processedRequests = PrinterI.getRequestProcess();

            if (!jitterList.isEmpty()) {
                long totalJitter = jitterList.stream().mapToLong(Long::longValue).sum();
                long averageJitter = totalJitter / jitterList.size();

                System.out.println("\n--------Performance:--------------- \n");
                System.out.println("Throughput: " + count + " requests in the last minute");
                System.out.println("Average Jitter: " + averageJitter + " ns");
                System.out.println("Unprocess Requests: " + failedRequests+ " requests in the last minute");
                System.out.println("Processed Requests: " + processedRequests+ " requests in the last minute");
                System.out.println("-------------------------------------");
                System.out.println();
            } else {
                System.out.println("\n--------Performance:--------------- \n");
                System.out.println("Throughput: " + count + " requests in the last minute");
                System.out.println("No jitter data available.");
                System.out.println("Failed Requests: " + failedRequests);
                System.out.println("Processed Requests: " + processedRequests);
                System.out.println("-------------------------------------");
                System.out.println();
            }
            
            //Resetear contadores 
            PrinterI.resetRequestCount();
            PrinterI.resetFailedRequestCount();  
            PrinterI.ProcessedRequestCount();

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
