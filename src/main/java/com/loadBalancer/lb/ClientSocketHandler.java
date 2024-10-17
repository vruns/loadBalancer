package com.loadBalancer.lb;
import com.loadBalancer.lb.entity.Server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.*;
import java.net.Socket;


/**
 * Class to handle each client socket request and run it in new Thread
 */

public class ClientSocketHandler implements Runnable {
    private final Socket clientSocket;
    private final Server selectedServer;


    public ClientSocketHandler(Socket socket, Server selectedServer) {
        this.clientSocket = socket;
        this.selectedServer = selectedServer;
    }

    @Override
    public void run() {
        try (
                InputStream clientInput = clientSocket.getInputStream();
                OutputStream clientOutput = clientSocket.getOutputStream()
        ) {
            if (selectedServer == null) {
                send503Response(clientOutput);
                return;
            }

            System.out.println("Selected Server: " + selectedServer.getUrl());

            // Create a TCP connection with the backend server
            try (
                    Socket backendSocket = new Socket(selectedServer.getHost(), selectedServer.getPort());
                    InputStream backendInput = new BufferedInputStream(backendSocket.getInputStream());
                    OutputStream backendOutput = new BufferedOutputStream(backendSocket.getOutputStream())
            ) {
                // Start threads to handle bidirectional communication
                Thread clientToBackendThread = new Thread(() -> transferData(clientInput, backendOutput));
                Thread backendToClientThread = new Thread(() -> transferData(backendInput, clientOutput));

                clientToBackendThread.start();
                backendToClientThread.start();

                // Wait for both threads to finish
                clientToBackendThread.join();
                backendToClientThread.join();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Failed to close client socket: " + e.getMessage());
            }
        }
    }

    // transfer data between input and output streams
    private void transferData(InputStream input, OutputStream output) {
        try {
            int data;
            while ((data = input.read()) != -1) {
                output.write(data);
                output.flush();  // Ensure data is sent immediately
            }
        } catch (IOException e) {
            System.err.println("Error in data transfer: " + e.getMessage());
        }
    }

    // Send 503 Service Unavailable response to the client
    private void send503Response(OutputStream output) throws IOException {
        String response = "HTTP/1.1 503 Service Unavailable\r\n" +
                "Content-Length: 0\r\n\r\n";
        output.write(response.getBytes());
        output.flush();

    }
}