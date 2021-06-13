package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.NeighbourMetadata;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public class AliveMessageHandler implements MessageHandler {
    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        //в теории никогда не null
        if (message.getSenderAddress() == null) {
            return;
        }

        //вариант 1
//        if (chatNode.neighbours.containsKey(message.getSenderAddress())) {
//            chatNode.neighbours.get(message.getSenderAddress()).setAlive(true);
//        }
        //вариант 2 с compute if present
//          избыточно, т.к. новый сосед живв
//            var newNeighbourMetadata = new NeighbourMetadata(metadata.getVice(), metadata.getName());
//            newNeighbourMetadata.setAlive(true);
//            return newNeighbourMetadata;
        chatNode.neighbours.computeIfPresent(message.getSenderAddress(),
                (address, metadata) -> new NeighbourMetadata(metadata.getVice(), metadata.getName()));

    }
}
