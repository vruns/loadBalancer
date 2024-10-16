package com.loadBalancer.lb;

import com.loadBalancer.lb.entity.Server;
import com.loadBalancer.lb.service.impl.RoundRobinAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoundRobinTest {

    private RoundRobinAlgorithm serverSelection;
    private List<Server> servers;

    @BeforeEach
    void setup() {
        serverSelection = new RoundRobinAlgorithm();
        servers = Arrays.asList(
                new Server("localhost4", 8080,"/health"),
                new Server("localhost5", 8080,"/health")
        );
    }

    @Test
    void testRoundRobinSelection() {
        Server firstServer = serverSelection.selectServer(servers);
        Server secondServer = serverSelection.selectServer(servers);

        assertEquals("localhost4", firstServer.getHost());
        assertEquals(8080, firstServer.getPort());

        assertEquals("localhost5", secondServer.getHost());
        assertEquals(8080, secondServer.getPort());
    }

    @Test
    void testRoundRobinWrapAround() {
        serverSelection.selectServer(servers);  // 1st server
        serverSelection.selectServer(servers);  // 2nd server
        Server wrapAroundServer = serverSelection.selectServer(servers);  // 1st server again

        assertEquals("localhost4", wrapAroundServer.getHost());
        assertEquals(8080, wrapAroundServer.getPort());
    }
}
