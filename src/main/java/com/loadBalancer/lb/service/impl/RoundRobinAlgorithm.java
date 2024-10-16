package com.loadBalancer.lb.service.impl;

import com.loadBalancer.lb.entity.Server;
import com.loadBalancer.lb.service.ServerService;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinAlgorithm implements ServerService {
    private AtomicInteger index = new AtomicInteger(0);

    /**
     * @implNote : gets the destination server address based on Round Robin algorithm pattern
     * @param servers : list of all the registered servers in load balancer
     * @return
     */
    @Override
    public Server selectServer(List<Server> servers) {
        if (servers.isEmpty()) {
            return null;
        }
        int currentIndex = index.getAndUpdate(i -> (i + 1) % servers.size());
        return servers.get(currentIndex);
    }
}
