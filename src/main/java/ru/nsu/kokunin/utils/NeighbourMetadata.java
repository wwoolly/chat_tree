package ru.nsu.kokunin.utils;

import java.net.InetSocketAddress;

public class NeighbourMetadata {
    private InetSocketAddress vice;
    private String name;

    private boolean isAlive = true;

    //соседний узел по-умолчанию жив
    public NeighbourMetadata(InetSocketAddress vice, String name) {
        this.vice = vice;
        this.name = name;
    }

    public InetSocketAddress getVice() {
        return vice;
    }

    public String getName() {
        return name;
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
