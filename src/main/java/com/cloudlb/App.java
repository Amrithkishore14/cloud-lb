package com.cloudlb;

import com.cloudlb.backend.AutoLoadBalancer;
import com.cloudlb.backend.HostManager;
import com.cloudlb.gui.LoginScreen;
import javafx.application.Application;

public class App {
    public static void main(String[] args) {
        // Start backend services in background (don’t crash UI if MQTT missing)
        new Thread(() -> {
            try { HostManager.main(new String[]{}); } catch (Throwable ignored) {}
        }, "HostManagerThread").start();

        new Thread(() -> {
            try { AutoLoadBalancer.main(new String[]{}); } catch (Throwable ignored) {}
        }, "AutoLoadBalancerThread").start();

        // Launch UI
        Application.launch(LoginScreen.class, args);
    }
}

