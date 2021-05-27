package ru.nsu.kokunin.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.utils.NeighbourData;
import ru.nsu.kokunin.utils.JsonConverter;
import ru.nsu.kokunin.utils.Message;
import ru.nsu.kokunin.utils.MessageMetadata;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

class Sender {
    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);

    private final NetworkController controller;
    private final DatagramSocket socket;
    private final List<NeighbourData> neighbours;

    private final JsonConverter jsonConverter = new JsonConverter();

    Sender(NetworkController controller, DatagramSocket socket, List<NeighbourData> neighbours) {
        this.controller = controller;
        this.socket = socket;
        this.neighbours = neighbours;
    }

    public void send(Message message, InetSocketAddress receiver) {
        try {
            byte[] rawMessage = jsonConverter.toJson(message).getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(rawMessage, rawMessage.length, receiver);
            socket.send(sendPacket);
        } catch (IOException exc) {
            LOG.error("Error during sending message <{}> to \"{}\"", message, receiver, exc);
        }
    }

    public void broadcast(MessageMetadata message) {
        neighbours.forEach(neighbour -> {
            if (neighbour.getAddress().equals(message.getSenderAddress())) {
                return;
            }

            if (message.isChecked()) {
                controller.addSentMessageToHistory(message.getMessage().getGUID(), message.getSenderAddress());
            }

            send(message.getMessage(), neighbour.getAddress());
        });
    }


}
