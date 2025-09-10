package tests;

import org.testng.TestNG;
import java.util.concurrent.*;

public class TestNGScheduler {

    public static void main(String[] args) {
        // Create a scheduler with 1 thread
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Define the task to run TestNG
        Runnable runTestNG = () -> {
            try {
                System.out.println("Running TestNG Suite at: " + java.time.LocalTime.now());

                // Create TestNG instance
                TestNG testng = new TestNG();
                
                // Set your TestNG XML file path here
                testng.setTestSuites(java.util.List.of("D:\\SeleniumDemo\\KestralPro\\testngAllinOneHybrid.xml"));
                
                // Run TestNG suite
                testng.run();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Schedule task: initial delay 0, then every 10 minutes
        scheduler.scheduleAtFixedRate(runTestNG, 0, 30, TimeUnit.MINUTES);
    }
}
