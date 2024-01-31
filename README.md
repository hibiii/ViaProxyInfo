# ViaProxyInfo

A plugin for ViaProxy that sends clients an informational packet about the proxied server as soon as they reach the `PLAY` state.

Licensed under MIT.

Currently, only a beta because I don't have enough confidence in the code that it'll work fine.

## Packet format

It's a custom payload packet with channel `viapxyinfo:query`, and a JSON object as the payload. It is sent only once when the proxy switches the client to play state.

The following fields are defined:
- `host`: the host of the remove server, as returned by `InetSocketAddress.getHostString()`.
- `port`: the port of the remove server, as returned by `InetSocketAddress.getPort()`.
- `version`: the protocol version number as the proxy is configured for.
