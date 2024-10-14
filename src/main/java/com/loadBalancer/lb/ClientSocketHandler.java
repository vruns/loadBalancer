package com.loadBalancer.lb;

import com.loadBalancer.lb.entity.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


/**
 * Class to handle each client socket request and run it in new Thread
 */
public class ClientSocketHandler implements Runnable{
    private Socket clientSocket;
    private Server selectedServer;

    public ClientSocketHandler(final Socket socket,final Server selectedServer){
        this.clientSocket=socket;
        this.selectedServer=selectedServer;
    }


    @Override
    public void run() {
        try {
            InputStream clientToLoadBalancerInputStream = clientSocket.getInputStream();
            OutputStream loadBalancerToClientOutputStream = clientSocket.getOutputStream();
            if (selectedServer == null) {
                System.out.println("HTTP / 503 Service Unavailable");
                return;
            } else {
                System.out.println("Selected Server : " + selectedServer.getUrl());
            }

            // Create a TCP Connection with the backend server
            Socket backendSocket = new Socket(selectedServer.getHost(), selectedServer.getPort());


            InputStream backendServerToLoadBalancerInputStream = backendSocket.getInputStream();
            OutputStream loadbalancerToBackendServerOutputStream = backendSocket.getOutputStream();

            Thread clientRequestHandler = new Thread() {
                public void run() {
                    try {
                        int data;
                        while ((data = clientToLoadBalancerInputStream.read()) != -1) {
                            loadbalancerToBackendServerOutputStream.write(data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            clientRequestHandler.start();

            Thread backendDataHandler = new Thread() {
                public void run() {
                    try {
                        int data;
                        while ((data = backendServerToLoadBalancerInputStream.read()) != -1) {
                            loadBalancerToClientOutputStream.write(data);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };


            backendDataHandler.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
