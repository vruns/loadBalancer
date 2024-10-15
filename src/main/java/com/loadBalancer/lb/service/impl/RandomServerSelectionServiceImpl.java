package com.loadBalancer.lb.service.impl;

import com.loadBalancer.lb.entity.Server;
import com.loadBalancer.lb.service.ServerService;

import java.util.*;

public class RandomServerSelectionServiceImpl implements ServerService {
    private final Random random = new Random();


    /**
     * @implNote : gets the destination server address based on Random Selection pattern
     * @param servers : list of all the registered servers in load balancer
     * @return
     */
    @Override
    public Server selectServer(List<Server> servers) {
        if (servers.isEmpty()) return null;
        return servers.get(random.nextInt(servers.size()));
    }
}
