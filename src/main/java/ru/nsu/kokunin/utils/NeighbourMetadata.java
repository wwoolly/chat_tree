package ru.nsu.kokunin.utils;

import java.net.InetSocketAddress;

public class NeighbourMetadata {
    private InetSocketAddress vice;

    private boolean isAlive;

    public NeighbourMetadata(InetSocketAddress vice, boolean isAlive) {
        this.vice = vice;
        this.isAlive = isAlive;
    }

    public InetSocketAddress getVice() {
        return vice;
    }

    public void setNewVice(InetSocketAddress vice) {
        this.vice = vice;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
