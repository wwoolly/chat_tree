package ru.nsu.kokunin.net.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.NeighbourMetadata;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public class AliveMessageHandler extends MessageHandler {

    @Override
    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        super.handle(message, chatNode);
        //вариант 1
//        if (chatNode.neighbours.containsKey(message.getSenderAddress())) {
//            chatNode.neighbours.get(message.getSenderAddress()).setAlive(true);
//        }
        //вариант 2 с compute if present
//          избыточно, т.к. новый сосед живв
//            var newNeighbourMetadata = new NeighbourMetadata(metadata.getVice(), metadata.getName());
//            newNeighbourMetadata.setAlive(true);
//            return newNeighbourMetadata;
        chatNode.getNeighbours().computeIfPresent(message.getSenderAddress(),
                (address, metadata) -> new NeighbourMetadata(metadata.getVice(), metadata.getName()));

    }
}
