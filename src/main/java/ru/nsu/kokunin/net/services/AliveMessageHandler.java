package ru.nsu.kokunin.net.services;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.MessageMetadata;
import ru.nsu.kokunin.utils.NeighbourMetadata;

import java.net.InetSocketAddress;

public class AliveMessageHandler implements MessageHandler {
    @Override
    public void handle(MessageMetadata message, ChatNode chatNode) {
        if (message.getSenderAddress() == null) {
            return;
        }

        if (chatNode.neighbours.containsKey(message.getSenderAddress())) {
            chatNode.neighbours.get(message.getSenderAddress()).setAlive(true);
        }
    }
}
