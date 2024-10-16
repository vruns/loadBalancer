
# High-Level Design (HLD)

## 1. Overview  
The load balancer distributes HTTP requests among multiple backend servers to ensure even load distribution and high availability. It supports multiple algorithms (default: round-robin) and can handle concurrent requests efficiently. It provides dynamic server registration/removal, error handling, and debugging capabilities.

## 2. Architecture Diagram  

### Architecture Components:
```
+---------------------+            +---------------------+
|     Client 1        |            |     Client N        |
| (Sends HTTP Request)|     . . .  | (Sends HTTP Request)|
+---------------------+            +---------------------+
               |                                |
               v                                v
        +-------------------------------------------+
        |              Load Balancer                |
        |   - HTTP Listener (Socket Server)         |
        |   - Load Balancing Algorithm Handler      |
        |   - Backend Server Registry               |
        +-------------------------------------------+
                          |
    -------------------------------------------------
    |                     |                         |
+-----------+      +-----------+            +-----------+
|  Server 1 |      |  Server 2 |            |  Server N |
| (Backend) |      | (Backend) |            | (Backend) |
+-----------+      +-----------+            +-----------+
```

## 3. Key Features  
- **Round-Robin Algorithm** (default): Distributes requests sequentially across all servers.
- **Random Algorithm**: Optional support for randomized load distribution.
- **Concurrent Request Handling**: Uses multi-threading to handle requests.
- **Dynamic Server Registration and Removal**: New servers can be added or removed on-the-fly.
- **Error Handling**: Manages server failures gracefully.
- **Monitoring and Debugging**: Logs errors and requests for easier troubleshooting.
- **Optional Health Check**: Ensures only healthy servers receive requests.

## 4. High-Level Call Flow  
1. **Clients** send HTTP requests to the load balancer.
2. **Load Balancer Listener** accepts the request and forwards it to the selected backend server.
3. **Algorithm Handler** (e.g., Round-Robin) determines which server to route the request to.
4. **Response** from the backend server is relayed back to the client.
5. **Errors** are logged and handled if the server fails to respond.
6. **Monitoring** tools track load balancer performance.
