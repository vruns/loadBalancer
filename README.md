# Load Balancer

## Overview

This project is a simple load balancer that distributes HTTP requests among a set of backend servers using the Round-Robin algorithm by default. It supports switching to other algorithms like Random selection and can dynamically add or remove backend servers.

### Features

- Round-robin and Random selection algorithms for load balancing.
- Dynamic addition and removal of backend servers.
- Handles multiple concurrent requests.
- Optional health checker to monitor backend server availability.

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/load-balancer.git
   cd load-balancer

2. Install dependencies (Maven):

   ```bash
   mvn clean install

3. Update the config.json file to include your backend servers.
4. Upate the Load Balancer port in LbApplication.java to the desired LB port

5. Run the load balancer:

    ```bash
   mvn exec:java -Dexec.mainClass="loadbalancer.LbApplication"
