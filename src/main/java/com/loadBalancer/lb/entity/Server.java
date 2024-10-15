package com.loadBalancer.lb.entity;


/**
 * This class is entity representation of a Server, containing
 * Host: the private ip of the instance of the backend server
 * Port: represents the  port on which the backend server application is running
 */
public class Server {
    private String host;
    private int port;
    private String healthCheckEndpoint;
    public Server(String host,int port,String healthCheckEndpoint){
        this.host=host;
        this.port=port;
        this.healthCheckEndpoint=healthCheckEndpoint;
    }

    public String getHost(){
        return host;
    }

    public int getPort(){
        return port;
    }

    public String getUrl(){
        return "https://"+host+":"+port;
    }

    public String geHealthCheckUrl(){
        return "https://"+host+":"+port+"/"+healthCheckEndpoint;
    }

}
