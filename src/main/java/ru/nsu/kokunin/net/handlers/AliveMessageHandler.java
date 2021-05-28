package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.MessageMetadata;

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