package ru.nsu.kokunin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SocketException{
        String name = args[0];
        int port = Integer.parseInt(args[1]);
        int loseRatio = Integer.parseInt(args[2]);

        DatagramSocket socket;
        try {
            socket = new DatagramSocket(port);
            log.debug("Socket started '{}'", socket);
        } catch (SocketException exc) {
            exc.printStackTrace();
            throw exc;
        }

        ChatNode chatNode = new ChatNode(name, loseRatio, socket);
        log.debug("ChatNode created");
        chatNode.init();
    }
}
