# eqd-slots-backend

This project is the backend for the EQD slots application.
It interfaces with the Stellar blockchain through the horizon network (Stellar SDK)
Game state is stored in a mysql database and several REST endpoints are exposed to the frontend to make it work.

In the production setup that it runs the backend is proxied by an AWS API gateway.

See https://equid.co/spin-to-win/ for the result
