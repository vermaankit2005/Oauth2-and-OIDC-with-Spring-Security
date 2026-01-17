# Keycloak & GitHub Integration Setup Guide

This guide walks you through configuring your local Keycloak instance to work with the prepared Spring Boot applications.

## 1. Access the Administration Console
1. Ensure Keycloak is running (`docker run` or `docker-compose up`).
2. Open your browser to: **[http://localhost:8080](http://localhost:8080)**.
3. Click **"Administration Console"**.
4. Log in with:
   - **Username**: `admin`
   - **Password**: `admin`

---

## 2. Create the Realm
*A Realm manages a set of users, credentials, roles, and groups.*

1. Hover over the **"Master"** label in the top-left corner.
2. Click **"Create Realm"**.
3. **Realm name**: `myrealm`
4. Click **Create**.

---

## 3. Register GitHub OAuth App (External Step)
*You need to tell GitHub to trust your Keycloak instance.*

1. Log in to your GitHub account.
2. Go to **Settings** (Top right profile icon) → **Developer settings** (Bottom left) → **OAuth Apps**.
3. Click **"New OAuth App"**.
4. Fill in the form:
   - **Application Name**: `Spring Boot Keycloak Demo`
   - **Homepage URL**: `http://localhost:8081` (The address of your BFF app)
   - **Authorization callback URL**: 
     ```
     http://localhost:8080/realms/myrealm/broker/github/endpoint
     ```
     *(Note: This URL points to Keycloak, not your Spring App. GitHub talks to Keycloak, Keycloak talks to Spring).*
5. Click **Register application**.
6. **Important**: Copy the **Client ID** and generate/copy the **Client Secret**. Keeping this tab open is helpful.

---

## 4. Configure GitHub Provider in Keycloak
1. Return to the Keycloak Console (Realm `myrealm`).
2. In the left menu, click **Identity providers**.
3. Click on the **GitHub** tile.
4. Fill in the credentials from step 3:
   - **Client ID**: *(Paste from GitHub)*
   - **Client Secret**: *(Paste from GitHub)*
5. Click **Add**.

---

## 5. Create the "BFF Client"
*This allows your Spring Boot BFF application to talk to Keycloak.*

1. In the left menu, click **Clients**.
2. Click **Create client**.
3. **Step 1: General Settings**:
   - **Client type**: `OpenID Connect`
   - **Client ID**: `bff-client`
   - Click **Next**.
4. **Step 2: Capability config**:
   - **Client authentication**: **ON** (This makes it "Confidential" - vital for BFFs).
   - **Authorization**: **OFF**.
   - **Authentication flow**: Select `Standard flow` (Authorization Code Flow).
   - Click **Next**.
5. **Step 3: Access settings**:
   - **Root URL**: `http://localhost:8081`
   - **Valid redirect URIs**: 
     ```
     http://localhost:8081/login/oauth2/code/keycloak
     ```
     *(This is the default endpoint Spring Security uses to receive the code).*
   - **Valid post logout redirect URIs**: `http://localhost:8081` (**Crucial for Logout**)
   - **Web origins**: `+` (or `http://localhost:8081`).
   - Click **Save**.

### 🔑 Get the Client Secret
1. After saving, go to the **Credentials** tab (top of the client page).
2. Locate the **Client secret** field.
3. Click the "Copy" icon.
4. **ACTION REQUIRED**: Open your local file `bff-service/src/main/resources/application.yml` and paste this secret into the `client-secret` field.

---

## 6. Create the "Resource Client" (Optional but Recommended)
*Used if you want to define specific permissions/roles for the API.*

1. Go to **Clients** → **Create client**.
2. **Client ID**: `resource-api`
3. **Capability config**:
   - **Client authentication**: **OFF** (Public).
   - **Authorization**: **OFF**.
4. **Access settings**:
   - Leave defaults or set valid redirect URIs to empty (since this API is stateless and doesn't do logins).
5. Click **Save**.

---

## 7. Testing the Setup
1. Restart your Spring Boot BFF application (to load the new secret).
2. Open incognito browser -> **http://localhost:8081/api/me**.
3. You should be redirected to Keycloak.
4. Click the "GitHub" button.
5. Log in with GitHub.
6. Keycloak redirects you back to the BFF.
7. BFF calls Resource Server.
8. **Success**: You see a message like `Protected Data for user: app-user [Verified by Keycloak]`.
