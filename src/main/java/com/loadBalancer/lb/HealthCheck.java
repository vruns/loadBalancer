package com.loadBalancer.lb;

import com.loadBalancer.lb.entity.Server;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class HealthCheck implements Runnable{

    private List<Server> servers;

    // Default interval set to 5 secs
    private int checkInterval = 5000;

    public HealthCheck(List<Server> servers) {
        this.servers = servers;
    }
    @Override
    public void run() {
        while (true) {
            for (Server server : servers) {
                if (!isServerHealthy(server)) {
                    System.out.println("Server " + server.getHost() + ":" + server.getPort() + " is down");
//                    Can Add Functionality to replace server
//                    servers.remove(server);
                }
            }
            try {
                Thread.sleep(checkInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isServerHealthy(Server server) {
        try {
            URL url = new URL(server.geHealthCheckUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
