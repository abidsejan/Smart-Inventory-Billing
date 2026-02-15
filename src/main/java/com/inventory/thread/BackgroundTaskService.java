package com.inventory.thread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Using a LinkedList for a recent activities log
public class BackgroundTaskService {
    private static BackgroundTaskService instance;
    private final ScheduledExecutorService scheduler;
    private final LinkedList<String> activityLog;

    private BackgroundTaskService() {
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.activityLog = new LinkedList<>();
        startTasks();
    }

    public static synchronized BackgroundTaskService getInstance() {
        if (instance == null) {
            instance = new BackgroundTaskService();
        }
        return instance;
    }

    private void startTasks() {
        // Task 1: Auto-save activity logs every 60 seconds
        scheduler.scheduleAtFixedRate(this::saveActivityLogs, 1, 60, TimeUnit.SECONDS);

        // Task 2: Check for low-stock alerts every 5 minutes
        scheduler.scheduleAtFixedRate(this::checkLowStockAlerts, 1, 5, TimeUnit.MINUTES);
    }

    public void logActivity(String activity) {
        synchronized (activityLog) {
            activityLog.addFirst(java.time.LocalTime.now() + ": " + activity);
            // Keep log size manageable
            if (activityLog.size() > 100) {
                activityLog.removeLast();
            }
        }
        System.out.println("Activity Logged: " + activity);
    }

    private void saveActivityLogs() {
        synchronized (activityLog) {
            if (activityLog.isEmpty()) return;
            System.out.println("AUTO-SAVING LOGS...");
            // In a real app, write this to a file
            // ReportGenerator.appendLogsToFile(activityLog);
            activityLog.clear();
        }
    }

    private void checkLowStockAlerts() {
        System.out.println("CHECKING FOR LOW STOCK ALERTS...");
        // In a real app, you would call InventoryService.getLowStockItems()
        // and then show a desktop notification or update the UI.
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}