package com.loadBalancer.lb;

import com.loadBalancer.lb.entity.Server;
import com.loadBalancer.lb.service.impl.RandomAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class RandomSelectionTest {

    private RandomAlgorithm serverSelection;
    private List<Server> servers;

    @BeforeEach
    void setup() {
        serverSelection = new RandomAlgorithm();
        servers = Arrays.asList(
                new Server("localhost4", 8080,"/health"),
                new Server("localhost5", 8080,"/health")
        );
    }

    @Test
    public void testRandomSelection() {
        Server selectedServer = serverSelection.selectServer(servers);
        assertTrue(servers.contains(selectedServer));
    }
}
