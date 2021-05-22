package ru.nsu.kokunin;

import ru.nsu.kokunin.node.TreeNode;
import ru.nsu.kokunin.services.Receiver;

import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class Main {
    public static TreeNode parseArgs(String[] args) {
        String name = args[0];
        int port = Integer.parseInt(args[1]);
        int loseRatio = Integer.parseInt(args[2]);

        return new TreeNode(name, port, loseRatio);
    }

    public static void main(String[] args) throws SocketException{
        TreeNode node = parseArgs(args);

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(node.getPort());
        } catch (SocketException exc) {
            exc.printStackTrace();
            throw exc;
        }

        Receiver receiver = new Receiver(socket);

        Chat chat = new Chat(node, receiver);
        chat.init();
    }
}
