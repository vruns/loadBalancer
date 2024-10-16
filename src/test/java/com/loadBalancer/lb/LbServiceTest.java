package com.loadBalancer.lb;

import com.loadBalancer.lb.entity.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LbServiceTest {
    @Mock
    private Socket mockClientSocket;
    private Socket mockBackendSocket;

    private Server mockBackendServer;
    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        // Initialize a mock backend server with dummy host and port
        mockBackendServer = new Server("localhost", 8080, "actuator/health");

        mockBackendSocket= new Socket("localhost",8080);

        // Mock the input/output streams for client and backend sockets
        when(mockClientSocket.getInputStream()).thenReturn(new ByteArrayInputStream("GET /test HTTP/1.1".getBytes()));
        when(mockClientSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
    }

    @Test
    public void testClientToBackendCommunication() throws IOException {
        // Create an instance of ClientSocketHandler with the mocked sockets
        ClientSocketHandler clientSocketHandler = new ClientSocketHandler(mockClientSocket, mockBackendServer);

        // Run the socket handler logic in a thread (simulating concurrent handling)
        Thread handlerThread = new Thread(clientSocketHandler);
        handlerThread.start();

        // Ensure the thread finishes execution
        try {
            handlerThread.join();
        } catch (InterruptedException e) {
            fail("Thread interrupted: " + e.getMessage());
        }

        InputStream mockInputStream = new ByteArrayInputStream("Test Data 1234".getBytes());
        when(mockClientSocket.getInputStream()).thenReturn(mockInputStream);
    }

    @Test
    public void testBackendServerUnavailable() throws IOException {
        // Create an instance with no backend server (simulate 503 response scenario)
        ClientSocketHandler clientSocketHandler = new ClientSocketHandler(mockClientSocket, null);

        // Run the socket handler logic in a thread
        Thread handlerThread = new Thread(clientSocketHandler);
        handlerThread.start();

        // Ensure the thread finishes execution
        try {
            handlerThread.join();
        } catch (InterruptedException e) {
            fail("Thread interrupted: " + e.getMessage());
        }

        // Verify that the correct response is sent to the client
        ByteArrayOutputStream clientOutput = (ByteArrayOutputStream) mockClientSocket.getOutputStream();
        String response = clientOutput.toString();

        assertTrue(response.contains("503 Service Unavailable"));
    }
}
