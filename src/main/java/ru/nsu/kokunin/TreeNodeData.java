package ru.nsu.kokunin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeNodeData {
    private final String name;
    private final int port;
    private final int loseRatio;

    private final List<NeighbourNode> neighbours;

    public TreeNodeData(String name, int port, int loseRatio) {
        //maybe check after Node starting
        if (port < 0 || port > 0xFFFF) throw new IllegalArgumentException("Incorrect port! Value: " + port);
        if (loseRatio < 0 || loseRatio > 99) throw new IllegalArgumentException("Incorrect lose ratio! Value: " + loseRatio + '!');

        this.name = name;
        this.port = port;
        this.loseRatio = loseRatio;

        neighbours = Collections.synchronizedList(new ArrayList<NeighbourNode>(0));
    }

    public synchronized boolean addNewNeighbour(NeighbourNode neighbour) {
        //надо?
//        if (neighbour == null || neighbours.contains(neighbour)) return false;

        return neighbours.add(neighbour);
    }

    public synchronized boolean removeNeighbour(NeighbourNode neighbour) {
        return neighbours.remove(neighbour);
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
