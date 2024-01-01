# Kirje
    This is a server side for a simple messaging application Kirje
    For Android side of application refer to [Kirje Mobile](https://github.com/q-13t/KirjeMobileApp).

## Server side
When Server side is started users can direct them selves to `[IP]:8080` for browser side where QR code will be displayed for mobile devices to connect.

In this implementation of app for simplicity all users are connected to single WebSocket that allows to transfer messages.

In order for mobile clients and another WEB clients to connect from remote devices Firewall needs to be configured to allow `UDP` connections on ports in range `4000-10000`. With this in mind server will allocate a WebSocket each time server is started on ports in this range.

## Features

Users can exchange plain text messages as well as files in formats:

- png
- jpg
- jpeg
- mp4