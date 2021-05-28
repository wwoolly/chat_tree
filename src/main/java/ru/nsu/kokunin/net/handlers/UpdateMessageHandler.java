package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.AddressStringPacker;
import ru.nsu.kokunin.utils.MessageMetadata;
import ru.nsu.kokunin.utils.NeighbourMetadata;

import java.net.InetSocketAddress;

public class UpdateMessageHandler implements MessageHandler {
    @Override
    public void handle(MessageMetadata message, ChatNode chatNode) {
        InetSocketAddress newVice = AddressStringPacker.unpackAddress(message.getMessage().getText());
        if (newVice != null) {
            NeighbourMetadata newMetadata = new NeighbourMetadata(newVice, message.getMessage().getName());
            chatNode.updateNeighbourMetadata(message.getSenderAddress(), newMetadata);
        }
    }
}
