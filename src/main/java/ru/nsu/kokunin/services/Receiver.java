package ru.nsu.kokunin.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.NetworkController;
import ru.nsu.kokunin.utils.JsonConverter;
import ru.nsu.kokunin.utils.Message;
import ru.nsu.kokunin.utils.MessageMetadata;
import ru.nsu.kokunin.utils.MessageType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Receiver implements Runnable {
    private static final int BUFFER_SIZE = 524288;
    private static final int MAX_LOSE_VALUE = 99;

    private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

    private final NetworkController controller;
    private final DatagramSocket socket;
    private final int loseRatio;

    private final JsonConverter jsonConverter = new JsonConverter();
    private final Random randomMessageLostGenerator = new Random();
    private final byte[] receiverBuffer = new byte[BUFFER_SIZE];

    private volatile boolean isActive = true;

    public Receiver(NetworkController controller, DatagramSocket socket, int loseRatio) {
        this.controller = controller;
        this.socket = socket;
        this.loseRatio = loseRatio;
    }

    /*public byte[] receiveMessage() {
        byte[] rawMessage = null;
        DatagramPacket packetToReceive = new DatagramPacket(receiverBuffer, BUFFER_SIZE);

        try {
            socket.receive(packetToReceive);
            rawMessage = packetToReceive.getData();
        } catch (IOException exc) {
            //TODO: add exception handler
            exc.printStackTrace();
        }

        return rawMessage;
    }*/

    @Override
    public void run() {
        Message message;
        InetAddress senderAddress;
        DatagramPacket packetToReceive = new DatagramPacket(receiverBuffer, BUFFER_SIZE);
        byte[] rawMessage = null;

        while(isActive) {
            try {
                socket.receive(packetToReceive);

                if (loseRatio > randomMessageLostGenerator.nextInt(MAX_LOSE_VALUE)) {
                    continue;
                }

                rawMessage = packetToReceive.getData();
                String jsonMessage = new String(rawMessage, StandardCharsets.UTF_16BE);
                message = jsonConverter.fromJson(jsonMessage, Message.class);
                senderAddress = packetToReceive.getAddress();

                controller.handleMessage(new MessageMetadata(message, senderAddress));
            } catch (IOException exc) {
                LOG.error("During socket receiving an exception occurred!", exc);
            }
        }
    }
}
