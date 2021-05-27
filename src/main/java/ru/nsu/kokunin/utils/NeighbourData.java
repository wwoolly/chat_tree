package ru.nsu.kokunin.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class NeighbourData {
    final private InetSocketAddress address;
    final private int port;

    NeighbourData(InetSocketAddress address, int port) {
        //maybe check after Node starting
        if (port < 0 || port > 0xFFFF) throw new IllegalArgumentException("Incorrect port! Value: " + port);

        this.address = address;
        this.port = port;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
