package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.AddressStringPacker;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public class GetMessageHandler extends MessageHandler {
    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        chatNode.getSender().sendACKMessage(message.getMessage().getGUID(), message.getSenderAddress());

        //не совсем красиво, зато просто
        if (chatNode.getVice() == null) {
            return;
        }

        String viceData = AddressStringPacker.packAddress(chatNode.getVice());
        chatNode.getSender().sendUpdateMessage(viceData, message.getSenderAddress());
    }
}
