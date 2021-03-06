package ru.nsu.kokunin.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.JsonConverter;
import ru.nsu.kokunin.utils.Message;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Receiver implements Runnable {
    private static final int BUFFER_SIZE = 524288;
    private static final int MAX_LOSE_VALUE = 99;

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    private final ChatNode chatNode;
    private final DatagramSocket socket;
    private final int loseRatio;

    private final JsonConverter jsonConverter = new JsonConverter();
    private final Random randomMessageLostGenerator = new Random();
    private final byte[] receiverBuffer = new byte[BUFFER_SIZE];

    private volatile boolean isActive = true;

    public Receiver(ChatNode chatNode, DatagramSocket socket, int loseRatio) {
        this.chatNode = chatNode;
        this.socket = socket;
        this.loseRatio = loseRatio;
    }

    public void stop() {
        isActive = false;
    }


    @Override
    public void run() {
        log.info("Receiver started");

        DatagramPacket packetToReceive = new DatagramPacket(receiverBuffer, BUFFER_SIZE);
        byte[] rawMessage;
        Message message;
        InetSocketAddress senderAddress;

        while(isActive) {
            try {
                log.debug("Trying to receive...");
                socket.receive(packetToReceive);
                log.debug("Received!");

                if (loseRatio > randomMessageLostGenerator.nextInt(MAX_LOSE_VALUE)) {
                    continue;
                }

                rawMessage = packetToReceive.getData();
                String jsonMessage = new String(rawMessage, StandardCharsets.UTF_8);
                message = jsonConverter.fromJson(jsonMessage, Message.class);
                senderAddress = (InetSocketAddress) packetToReceive.getSocketAddress();

                chatNode.handleMessage(new ReceivedMessageMetadata(message, senderAddress));
            } catch (IOException exc) {
                log.error("During socket receiving an exception occurred!", exc);
            } finally {
                packetToReceive.setLength(BUFFER_SIZE);
            }
        }
    }
}
