package ru.nsu.kokunin.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodeData {
    private final String name;
    private final int port;
    private final int loseRatio;

    public NodeData(String name, int port, int loseRatio) {
        //maybe check after Node starting
        if (port < 0 || port > 0xFFFF) throw new IllegalArgumentException("Incorrect port! Value: " + port);
        if (loseRatio < 0 || loseRatio > 99) throw new IllegalArgumentException("Incorrect lose ratio! Value: " + loseRatio + '!');

        this.name = name;
        this.port = port;
        this.loseRatio = loseRatio;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public int getLoseRatio() {
        return loseRatio;
    }
}
