package com.loadBalancer.lb.service;

import com.loadBalancer.lb.entity.Server;
import java.util.*;

public interface ServerService {

    /**
     * This method gets the destination server for a HTTP request based on the selected algo implementation
     * @param servers : list of all the registered servers in load balancer
     * @return : destination Server entity where the HTTP request needs to be redirected
     */
    Server selectServer(List<Server> servers);
}
