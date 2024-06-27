package com.mascherpa.diadepesca.network;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CheckInternet {
    public Boolean isOnline() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
                int returnVal = p1.waitFor();
                return (returnVal == 0);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        });
        try {
            return future.get(); // Espera a que el resultado esté disponible
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            executor.shutdown();
        }
    }
}

