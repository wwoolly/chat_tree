package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.AddressStringPacker;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;
import ru.nsu.kokunin.utils.NeighbourMetadata;

import java.net.InetSocketAddress;

public class StartMessageHandler extends MessageHandler {
    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        super.handle(message, chatNode);
        super.log.info("Received START message from node '{}'; address: {}",
                message.getMessage().getName(), message.getSenderAddress());

        chatNode.getSender().sendACKMessage(message.getMessage().getGUID(), message.getSenderAddress());

        if (chatNode.getNeighbours().containsKey(message.getSenderAddress())) {
            super.log.info("Node '{}' with address: {} is already registered as a neighbour",
                    message.getMessage().getName(), message.getSenderAddress());
            return;
        }

        //TODO а что если заместитель null
        InetSocketAddress newNeighbourVice = AddressStringPacker.unpackAddress(message.getMessage().getText());

        NeighbourMetadata newNeighbour = new NeighbourMetadata(newNeighbourVice, message.getMessage().getName());
        chatNode.registerNewNeighbour(message.getSenderAddress(), newNeighbour);
    }
}
