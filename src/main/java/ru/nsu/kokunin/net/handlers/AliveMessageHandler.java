package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public class AliveMessageHandler implements MessageHandler {
    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        //в теории никогда не null
        if (message.getSenderAddress() == null) {
            return;
        }

        if (chatNode.neighbours.containsKey(message.getSenderAddress())) {
            chatNode.neighbours.get(message.getSenderAddress()).setAlive(true);
        }
    }
}
