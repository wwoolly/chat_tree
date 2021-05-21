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
    private final NodeData data;
    private final List<NeighbourData> neighbours;

    //GUID - message
    private ConcurrentMap<String, MessageMetadata> messages = new ConcurrentHashMap<>();


    public TreeNode(NodeData data) {
        this.data = data;
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
        Message message = new Message(data.getName(), messageText);
        MessageMetadata messageMetadata = new MessageMetadata(message, null);
        messages.put(message.getGUID(), messageMetadata);
    }
}
