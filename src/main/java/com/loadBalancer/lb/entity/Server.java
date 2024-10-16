package com.loadBalancer.lb.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is entity representation of a Server, containing
 * Host: the private ip of the instance of the backend server
 * Port: represents the  port on which the backend server application is running
 */
public class Server {
    private String host;
    private int port;
    private String healthCheckEndpoint;

    @JsonCreator
    public Server(@JsonProperty("host") String host,@JsonProperty("port") int port,@JsonProperty("healthCheckEndpoint") String healthCheckEndpoint){
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
    public String getHealthCheckEndpoint(){return healthCheckEndpoint;};

    public String getUrl(){
        return "https://"+host+":"+port;
    }

    public String geHealthCheckUrl(){
        return "https://"+host+":"+port+"/"+healthCheckEndpoint;
    }

}
