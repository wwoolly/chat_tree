package ru.nsu.kokunin.net.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.NeighbourMetadata;
import ru.nsu.kokunin.utils.SentMessageMetadata;

import java.net.InetSocketAddress;

public class NeighbourChecker implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(NeighbourChecker.class);

    private final ChatNode chatNode;

    public NeighbourChecker(ChatNode chatNode) {
        this.chatNode = chatNode;
    }

    @Override
    public void run() {
        var neighbourIterator = chatNode.neighbours.entrySet().iterator();
        InetSocketAddress vice;

        while (neighbourIterator.hasNext()) {
            var entry =  neighbourIterator.next();
            vice = entry.getValue().getVice();

            if (!entry.getValue().isAlive()) {
                if (vice != null) {
                    chatNode.neighbours.put(vice, new NeighbourMetadata(null, null));
                    chatNode.sender.sendGetMessage(vice);
                }

                clearSentMessages(entry.getKey());

                neighbourIterator.remove();
                log.info("Neighbor <{}> [{}] is dead, removed", entry.getValue().getName(), entry.getKey());

                isNeighbourVice(entry.getKey());
            }
        }

        chatNode.neighbours.forEach((k, v) -> v.setAlive(false));
    }

    private void clearSentMessages(InetSocketAddress neighbourAddress) {
        //вынести в отдельный метод ChatNode
        var sentMessages = chatNode.sentMessages;
        var iterator = sentMessages.entrySet().iterator();

        while(iterator.hasNext()) {
            SentMessageMetadata metadata = iterator.next().getValue();
            if (metadata.hasSentTo(neighbourAddress)) {
                metadata.removeReceiver(neighbourAddress);
                if (metadata.hasNoReceivers()) {
                    iterator.remove();
                }
            }
        }
    }

    private void isNeighbourVice(InetSocketAddress neighbour) {
        if (neighbour.equals(chatNode.vice)) {
            chatNode.vice
                    = chatNode.neighbours.isEmpty()
                    ? null
                    : chatNode.neighbours.keySet().iterator().next();

            log.info("Set new vice for this node [{}]", chatNode.vice);
        }
    }
}
