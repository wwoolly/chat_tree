package ru.nsu.kokunin;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Main {
    public static void main(String[] args) throws SocketException{
        String name = args[0];
        int port = Integer.parseInt(args[1]);
        int loseRatio = Integer.parseInt(args[2]);

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException exc) {
            exc.printStackTrace();
            throw exc;
        }

        ChatNode chatNode = new ChatNode(name, loseRatio, socket);
        chatNode.init();
    }
}
