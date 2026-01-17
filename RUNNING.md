# How to Run the Project

## Prerequisites
*   **Java 17+** installed.
*   **Maven** installed (or use included wrapper if available).
*   **Docker** & **Docker Compose** installed and running.

---

## Step 1: Infrastructure Setup (Keycloak)

1.  **Start Keycloak**:
    Open a terminal in the project root and run:
    ```bash
    docker-compose up -d
    ```
    Wait for Keycloak to start (access [http://localhost:8080](http://localhost:8080) to check).

2.  **Configure Keycloak**:
    Follow the detailed steps in **[KEYCLOAK_SETUP.md](./KEYCLOAK_SETUP.md)** to:
    *   Create the `myrealm` realm.
    *   Configure GitHub Identity Provider.
    *   Create `bff-client` and get the **Client Secret**.

3.  **Update BFF Configuration**:
    Open `bff-service/src/main/resources/application.yml` and paste your client secret:
    ```yaml
    client-secret: <YOUR_KEYCLOAK_CLIENT_SECRET>
    ```

---

## Step 2: Run the Services

You will need two separate terminal windows.

### Terminal 1: Run BFF Service
```bash
cd bff-service
mvn spring-boot:run
```
*   **Port**: `8081`

### Terminal 2: Run Resource Service
```bash
cd resource-service
mvn spring-boot:run
```
*   **Port**: `8082`

---

## Step 3: Test the Flow

1.  Open your browser (try Incognito mode to ensure a fresh session).
2.  Navigate to: **[http://localhost:8081/api/me](http://localhost:8081/api/me)**
3.  **Login**:
    *   You will be redirected to the Keycloak login page.
    *   Click "GitHub" (or log in with a local Keycloak user if you created one).
4.  **Result**:
    *   After successful login, you will be redirected back to the BFF.
    *   The BFF will fetch the data from the Resource Server.
    *   You should see a response similar to:
        ```
        Protected Data for user: your-github-username [Verified by Keycloak]
        ```

## Troubleshooting

*   **Port 8080 already in use?**
    *   Check if another service is running. You can change the port in `docker-compose.yml`.
*   **"Connection refused"?**
    *   Ensure all 3 services (Keycloak, BFF, Resource) are running.
*   **401 Unauthorized from Resource Server?**
    *   The Token Relay might be failing. Check BFF logs.
    *   Ensure Keycloak Issuer URI matches in both `application.yml` files.
