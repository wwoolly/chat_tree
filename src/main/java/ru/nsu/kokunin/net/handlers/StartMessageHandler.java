package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.AddressStringPacker;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;
import ru.nsu.kokunin.utils.NeighbourMetadata;

import java.net.InetSocketAddress;

public class StartMessageHandler implements MessageHandler {
    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        if (chatNode.neighbours.containsKey(message.getSenderAddress())) {
            //TODO а это возможно?
            return;
        }

        //TODO а что если заместитель null
        InetSocketAddress newNeighbourVice = AddressStringPacker.unpackAddress(message.getMessage().getText());

        NeighbourMetadata newNeighbour = new NeighbourMetadata(newNeighbourVice, message.getMessage().getName());
        chatNode.registerNewNeighbour(message.getSenderAddress(), newNeighbour);
    }
}
