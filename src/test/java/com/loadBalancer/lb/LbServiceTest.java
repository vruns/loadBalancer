package com.loadBalancer.lb;

import com.loadBalancer.lb.entity.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

public class LbServiceTest {
    private Socket mockClientSocket;
    private Socket mockBackendSocket;
    private Server mockServer;
    private InputStream mockClientInput;
    private ByteArrayOutputStream mockClientOutput;
    private InputStream mockBackendInput;
    private ByteArrayOutputStream mockBackendOutput;

    @BeforeEach
    void setUp() throws IOException {
        // Mock client socket and streams
        mockClientSocket = mock(Socket.class);
        mockClientOutput = new ByteArrayOutputStream();
        mockClientInput = new ByteArrayInputStream("Client Request".getBytes());

        // Set up client input and output
        when(mockClientSocket.getInputStream()).thenReturn(mockClientInput);
        when(mockClientSocket.getOutputStream()).thenReturn(mockClientOutput);

        // Mock backend socket
        mockBackendSocket = mock(Socket.class);
        mockBackendOutput = new ByteArrayOutputStream();
        mockBackendInput = new ByteArrayInputStream("Backend Response".getBytes());

        // Mock backend input and output
        when(mockBackendSocket.getInputStream()).thenReturn(mockBackendInput);
        when(mockBackendSocket.getOutputStream()).thenReturn(mockBackendOutput);

        // Mock server object with host and port
        mockServer = new Server("localhost", 8080, "/health");
    }

    @Test
    void testClientToBackendCommunication() throws IOException, InterruptedException {
        // Create a handler with the mocked client socket and server
        ClientSocketHandler handler = new ClientSocketHandler(mockClientSocket, mockServer);

        // Mock the behavior of establishing a connection with the backend server
        try (Socket backendSocket = mockBackendSocket) {
            // Replace the original connection with the mock backend socket
            whenNew(Socket.class).withArguments("localhost", 8080).thenReturn(backendSocket);
            Thread handlerThread = new Thread(handler);
            handlerThread.start();
            handlerThread.join(); // Wait for the handler to finish

            // Now, assert that the client output contains the expected backend response
            assertEquals("Backend Response", mockClientOutput.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test503ResponseWhenNoServerAvailable() throws IOException, InterruptedException {
        // Create a handler with a null server to trigger 503 response
        ClientSocketHandler handler = new ClientSocketHandler(mockClientSocket, null);

        // Run the handler in a separate thread
        Thread handlerThread = new Thread(handler);
        handlerThread.start();
        handlerThread.join(); // Wait for the handler to finish

        // Check if the 503 response was sent to the client
        String expectedResponse = "HTTP/1.1 503 Service Unavailable\r\nContent-Length: 0\r\n\r\n";
        assertEquals(expectedResponse, mockClientOutput.toString());
    }
}
