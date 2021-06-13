package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public class ChatMessageHandler extends MessageHandler {

    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        super.handle(message, chatNode);

        chatNode.getSender().sendACKMessage(message.getMessage().getGUID(), message.getSenderAddress());

        chatNode.outChatMessage(message);

        message.setChecked(true);
        chatNode.getSender().broadcast(message);
    }
}
