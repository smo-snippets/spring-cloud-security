# Keycloak

We use keycloak as our OpenID Connect provider. To set it up:

## Running keycloak

1. Start Keycloak through `docker-compose up`.
2. Open http://localhost:8090 and login with username `admin` and password `Pa55w0rd`.
3. Add a new realm named `testrealm`.
4. Import `realm-export.json`.
5. On the `web-client` client Credentials tab, `Regenerate Secret` and store value in gateway `application.properties`.
6. Add a new user with username `user`.
7. Under credentials set user password to `password` with `Temporary: OFF`.

## What you'll get

* a realm named `testrealm`.
* a client named `web-client` with:
 - `Access Type: confidential` and `Credentials Secret:f37a4996-47f4-4c4c-aedc-c4dcde42b314` 
 - `Valid Redirect URIs: http://localhost:8765/*`
 - `Access Token Lifespan: 20 minutes`
* a user named `user`.

The gateway will authenticate with the client credentials to the realm.
When redirected by the gateway you can login with the user credentials.

## Cleaning up

1. Remove container through `docker-compose rm -f`.
2. Remove volume through `docker volume prune`.