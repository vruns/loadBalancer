
# Low-Level Design (LLD)

## 1. Class Diagram  
Below is a detailed class structure of the application.

```
+---------------------------------+
|        LoadBalancer             |
|---------------------------------|
| - servers: List<Server>         |
| - algorithm:ServerService       |  
|---------------------------------|
| + addServer(Server)             |
| + removeServer(Server)          |
| + changeAlgorithm(ServerService)|
| + loadServers(String)           |
| + start()                       |
+---------------------------------+

+-------------------------------+              +-------------------------+
|       Server                  |              |      ServerService      |
|-------------------------------|              |-------------------------|
| - host: String                |              | + selectServer()        |
| - port: int                   |              +-------------------------+
| - healthCheckEndpoint: String |
+-------------------------------+

+---------------------------+
|      RoundRobinAlgorithm  |
|---------------------------|
| + selectServer()          |
+---------------------------+

+---------------------------+
|       RandomAlgorithm     |
|---------------------------|
| + selectServer()          |
+---------------------------+



+-------------------------------------------------+
|                  ClientSocketHandler            |
+-------------------------------------------------+
| - clientSocket: Socket                          |
| - selectedServer: Server                        |
+-------------------------------------------------+
| + run(): void                                   |
| - transferData(input: InputStream,              |
|   output: OutputStream): void                   |
| - send503Response(output: OutputStream): void   |
+-------------------------------------------------+
```

## 2. Class Responsibilities  

### 1. LoadBalancer  
- **Attributes:**
  - `List<Server> servers`: Stores registered backend servers.
  - `ServerService algorithm`: Current load balancing algorithm (e.g., round-robin).

- **Methods:**
  - `addServer(Server server)`: Adds a new backend server to the list.
  - `removeServer(Server server)`: Removes a backend server.
  - `start()`: Starts the application by listening to HTTP request on the configured socket port.
  - `changeAlgorithm(ServerService newAlgo)`: Sets the load balancing algorithm.

### 2. Server  
- Represents a backend server with attributes like host,port and health check endpoint.  
- Methods:
  - `getUrl()`: Returns backend server host URL.
  - `geHealthCheckUrl()`: Returns health check url for server  

### 3. ServerService Interface (Algorithm)  
- **selectServer()**: Defines a method to select a server based on the algorithm.

### 4. RoundRobinAlgorithm  
- Implements the **ServerService** interface.  
- **selectServer()**: Returns the next server in a circular fashion.

### 5. RandomAlgorithm  
- Implements the **ServerService** interface.  
- **selectServer()**: Returns a random server from the list.

### 6. ClientSocketHandler
- `run()` : Manages a client connection by forwarding requests and responses between the client and a backend server over TCP.
  - Starts the data transfer logic in two threads
  - Client → Backend || Backend → Client 
  - Joins both threads to ensure communication completes. 
  - Handles socket closure and exceptions.
- `transferData(InputStream, OutputStream)`: Reads data from input and writes it to output continuously until EOF

## 3. Sequence Diagram  
```
Client -> LoadBalancer: Sends Request
LoadBalancer -> ServerService: selectServer()
ServerService -> LoadBalancer: Returns Server
LoadBalancer -> ClientSocketHandler: Forwards Request
ClientSocketHandler -> LoadBalancer: Sends Response
LoadBalancer -> Client: Forwards Response
```

## 4. Configuration Handling  
- **config.json** stores the list of backend servers. This can be loaded dynamically using Jackson.

Example:

```json
[
    { "address": "localhost", "port": 8080 },
    { "address": "127.0.0.1", "port": 9090 }
]
```


## Conclusion  
The load balancer application is designed to be extendable, scalable, and fault-tolerant. It can support multiple algorithms and dynamically manage backend servers. With proper threading, error handling, and logging, it ensures smooth and efficient load distribution among servers.
