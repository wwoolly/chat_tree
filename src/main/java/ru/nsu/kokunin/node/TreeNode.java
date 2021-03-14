package ru.nsu.kokunin.node;

import java.io.IOException;

public class TreeNode {
    private final TreeNodeData data;

    public TreeNode(TreeNodeData data) {
        this.data = data;
    }

    public void start() {

    }

    private boolean checkNode(NeighbourNode node) {
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
}
