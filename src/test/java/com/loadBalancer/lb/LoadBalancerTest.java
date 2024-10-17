package com.loadBalancer.lb;

import com.loadBalancer.lb.entity.Server;
import com.loadBalancer.lb.service.impl.RoundRobinAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadBalancerTest {
    private LoadBalancer loadBalancer;

    @BeforeEach
    void setup() throws Exception {
        loadBalancer = new LoadBalancer(8080, "src/main/resources/config.json", new RoundRobinAlgorithm());
    }

    @Test
    void testAddServer() throws IOException {
        Server newServer = new Server("localhost4", 8080,"/health");
        loadBalancer.addServer(newServer);

        assertEquals(3, loadBalancer.getServers().size());
    }

    @Test
    void testRemoveServer() throws IOException {
        Server serverToRemove =new Server("localhost4", 8080,"/health");
        loadBalancer.removeServer(serverToRemove);

        assertEquals(2, loadBalancer.getServers().size());
    }
}
