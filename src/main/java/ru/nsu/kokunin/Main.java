package ru.nsu.kokunin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.utils.NeighbourMetadata;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SocketException {
        String name = args[0];
        int port = Integer.parseInt(args[1]);
        int loseRatio = Integer.parseInt(args[2]);

        //test
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

        if (args.length > 3) {
            String neighbourAddress = args[3];
            int neighbourPort = Integer.parseInt(args[4]);
            log.info("neighbour detected in args: [{}:{}]", neighbourAddress, neighbourPort);

            try {
                InetSocketAddress socketAddress = new InetSocketAddress(neighbourAddress, neighbourPort);
                chatNode.neighbours.put(socketAddress, new NeighbourMetadata(null, null));
//                chatNode.registerNewNeighbour(socketAddress, new NeighbourMetadata(null, null));
            } catch (SecurityException exc) {
                log.error("Security exception during trying to parse input neighbour data", exc);
            } catch (IllegalArgumentException exc) {
                log.error("Incorrect input parameters: neighbour node address '{}' and '{}' port", neighbourAddress, neighbourPort, exc);
            }

        }
        chatNode.init();
    }
}
