package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public class ACKMessageHandler extends MessageHandler {
    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        super.handle(message, chatNode);

        String messageToConfirmGUID = message.getMessage().getText();
        chatNode.getSentMessages().remove(messageToConfirmGUID);
    }
}
