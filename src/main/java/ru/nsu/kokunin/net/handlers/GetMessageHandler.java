package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.AddressStringPacker;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public class GetMessageHandler implements MessageHandler {
    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        chatNode.sender.sendACKMessage(message.getMessage().getGUID(), message.getSenderAddress());

        //не совсем красиво, зато просто
        if (chatNode.vice == null) {
            return;
        }

        String viceData = AddressStringPacker.packAddress(chatNode.vice);
        chatNode.sender.sendUpdateMessage(viceData, message.getSenderAddress());
    }
}
