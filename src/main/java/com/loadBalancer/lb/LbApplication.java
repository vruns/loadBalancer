package com.loadBalancer.lb;

import com.loadBalancer.lb.service.ServerService;
import com.loadBalancer.lb.service.impl.RoundRobinServerSelectionServiceImpl;

import java.io.IOException;

public class LbApplication {

	public static void main(String[] args) throws IOException {
		ServerService serverSelection = new RoundRobinServerSelectionServiceImpl();  // Default to Round Robin
		LoadBalancer loadBalancer = new LoadBalancer(8081, "src/main/resources/config.json", serverSelection);
		loadBalancer.start();
	}

}
