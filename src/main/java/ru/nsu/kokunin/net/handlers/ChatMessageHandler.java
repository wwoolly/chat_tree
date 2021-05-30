package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public class ChatMessageHandler implements MessageHandler {

    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        chatNode.sender.sendACKMessage(message.getMessage().getGUID(), message.getSenderAddress());

        chatNode.outChatMessage(message);

        message.setChecked(true);
        chatNode.sender.broadcast(message);
    }
}
