package ru.nsu.kokunin.node;

import ru.nsu.kokunin.ui.MessageGetter;
import ru.nsu.kokunin.utils.Message;
import ru.nsu.kokunin.utils.MessageMetadata;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;

public class TreeNode {
    private final String name;
    private final int port;
    private final int loseRatio;
    private final List<NeighbourData> neighbours;

    //<GUID, message>
    private ConcurrentMap<String, MessageMetadata> messages = new ConcurrentHashMap<>();


    public TreeNode(String name, int port, int loseRatio) {
        //maybe check after Node starting
        if (port < 0 || port > 0xFFFF) throw new IllegalArgumentException("Incorrect port! Value: " + port);
        if (loseRatio < 0 || loseRatio > 99) throw new IllegalArgumentException("Incorrect lose ratio! Value: " + loseRatio + '!');

        this.name = name;
        this.port = port;
        this.loseRatio = loseRatio;
        this.neighbours = Collections.synchronizedList(new ArrayList<>(0));
    }

    public synchronized boolean addNewNeighbour(NeighbourData neighbour) {
        //надо?
//        if (neighbour == null || neighbours.contains(neighbour)) return false;

        return neighbours.add(neighbour);
    }

    public synchronized boolean removeNeighbour(NeighbourData neighbour) {
        return neighbours.remove(neighbour);
    }

    private boolean checkNode(NeighbourData node) {
//        try {
//            DatagramSocket datagramSocket = new DatagramSocket();
//            byte[] pingData = "PING".getBytes(StandardCharsets.UTF_8);
//            datagramSocket.connect(node.address, node.port);
//            DatagramPacket sendPacket = new DatagramPacket(pingData, pingData.length, node.address, node.port);
//            datagramSocket.send(sendPacket);
//            byte[] receiveData = new byte[8];
//            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//            datagramSocket.setSoTimeout(200);
//            datagramSocket.receive(receivePacket);
//            datagramSocket.close();
//        } catch (SocketTimeoutException e) {
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//        return false;

//        а может вот так???
//        try (DatagramSocket socket = new DatagramSocket(node.port)) {
//            socket.setReuseAddress(true);
//        } catch (IOException exc) {}

        try {
            return node.address.isReachable(200);
        } catch (IOException exc) {
            return false;
        }
    }

    public void cacheNewMessage(String messageText) {
        Message message = new Message(name, messageText);
        MessageMetadata messageMetadata = new MessageMetadata(message, null);
        messages.put(message.getGUID(), messageMetadata);
    }
}
