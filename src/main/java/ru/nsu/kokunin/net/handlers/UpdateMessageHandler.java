package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.AddressStringPacker;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;
import ru.nsu.kokunin.utils.NeighbourMetadata;

import java.net.InetSocketAddress;

public class UpdateMessageHandler extends MessageHandler {
    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        super.handle(message, chatNode);

        chatNode.getSender().sendACKMessage(message.getMessage().getGUID(), message.getSenderAddress());

        InetSocketAddress newVice = AddressStringPacker.unpackAddress(message.getMessage().getText());
        if (newVice != null) {
            NeighbourMetadata newMetadata = new NeighbourMetadata(newVice, message.getMessage().getName());
            chatNode.updateNeighbourMetadata(message.getSenderAddress(), newMetadata);
        }
    }
}
