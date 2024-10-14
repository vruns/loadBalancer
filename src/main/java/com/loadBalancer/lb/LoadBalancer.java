package com.loadBalancer.lb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loadBalancer.lb.entity.Server;
import com.loadBalancer.lb.service.ServerService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * This class is a implementation of our Load Balancer
 * Servers: list of all the registered servers
 * serverSelection: the alogrithm to be used for load balancing
 * Port: the port on which we want to run the LoadBalancer
 */
public class LoadBalancer {
    private List<Server> servers;
    private ServerService serverSelection;
    private int port;

    public LoadBalancer(int port, String configFilePath, ServerService serverSelection) throws IOException {
        this.port = port;
        this.serverSelection = serverSelection;
        this.servers = loadServers(configFilePath);
    }

    /**
     * @implNote : intialises the load balancer with all the servers available in the config file
     * @param configFilePath : path of the config json file containing the list of all the backend servers
     * @return : list of backedn servers
     * @throws IOException
     * NOTE : in config json Host variable is supposed to be the private ip of the remote instance and we are assuming the destination application is running on port 8080
     */
    public List<Server> loadServers(String configFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.asList(mapper.readValue(new File(configFilePath), Server[].class));
    }


    /**
     * @implNote : inserts/ registers new backend server
     * @param server
     */
    public void addServer(Server server) {
        servers.add(server);
    }


    /**
     * @implNote : removes a registered backend server
     * @param server
     */
    public void removeServer(Server server) {
        servers.remove(server);
    }

    /**
     * @implNote : change the scheduling algorithm type
     * @param newAlgo
     */
    public void changeSelector(ServerService newAlgo) {
        this.serverSelection = newAlgo;
    }


    /**
     * @implNote : Starts the load balancing service
     * @throws IOException
     */
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Load Balancer listening on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("TCP Connection established with client :" +clientSocket.toString());
            Server server= serverSelection.selectServer(servers);
            ClientSocketHandler clientSocketRequest = new ClientSocketHandler(clientSocket,server);
            Thread clientThread= new Thread(clientSocketRequest);
            clientThread.start();
        }
    }




}
