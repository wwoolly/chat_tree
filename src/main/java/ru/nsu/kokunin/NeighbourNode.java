package ru.nsu.kokunin;

import java.net.InetAddress;

public class NeighbourNode {
    final InetAddress address;
    final int port;

    NeighbourNode(InetAddress address, int port) {
        //maybe check after Node starting
        if (port < 0 || port > 0xFFFF) throw new IllegalArgumentException("Incorrect port! Value: " + port);

        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
