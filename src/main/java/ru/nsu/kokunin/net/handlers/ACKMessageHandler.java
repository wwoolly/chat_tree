package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public class ACKMessageHandler implements MessageHandler {
    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        String messageToConfirmGUID = message.getMessage().getText();
        chatNode.sentMessages.remove(messageToConfirmGUID);
    }
}
