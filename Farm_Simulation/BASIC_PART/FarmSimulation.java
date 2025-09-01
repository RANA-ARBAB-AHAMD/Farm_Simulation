import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class FarmSimulation {
    private static final int interval = 200;
    private static final int timeout = 3;

    public static void main(String[] args) {
        int rows = 14;
        int columns = 14;
        Farm farm = new Farm(rows, columns);

        farm.displayTheFarm();

        AtomicBoolean isRunning = new AtomicBoolean(true);
        int Noofthreads = farm.getDogList().size() + farm.getSheepList().size();

        ExecutorService executor = Executors.newFixedThreadPool(Noofthreads);

        try {

            farm.getSheepList().forEach(executor::execute);
            farm.getDogList().forEach(executor::execute);

            Thread displayThread = new Thread(() -> {
                try {
                    while (isRunning.get() && !farm.isSheepEscaped()) {
                        farm.displayTheFarm();
                        Thread.sleep(interval);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "DisplayThread");
            displayThread.start();

            displayThread.join();

        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {

            isRunning.set(false);
            shutdownExecutor(executor);
        }

        farm.displayTheFarm();
        System.out.println("Simulation ended." +
                (farm.isSheepEscaped() ? "Sheep " + farm.winnerSheep.getName() + " has escaped!"
                        : " Terminated by external signal."));
    }

    private static void shutdownExecutor(ExecutorService executor) {
        try {

            executor.shutdown();

            if (!executor.awaitTermination(timeout, TimeUnit.SECONDS)) {
                System.out.println("Some tasks are still running. Forcing shutdown...");

                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Shutdown interrupted: " + e.getMessage());
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
