package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.MessageMetadata;

public class ChatMessageHandler implements MessageHandler {

    @Override
    public void handle(MessageMetadata message, ChatNode chatNode) {
        chatNode.ioController.outMessage(message.getMessage());

        message.setChecked(true);
        chatNode.sender.broadcast(message);
    }
}
