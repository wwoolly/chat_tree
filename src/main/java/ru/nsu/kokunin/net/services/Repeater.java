package ru.nsu.kokunin.net.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.ChatNode;

public class Repeater implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(Repeater.class);

    private final ChatNode chatNode;

    public Repeater(ChatNode chatNode) {
        this.chatNode = chatNode;
    }

    @Override
    public void run() {
        chatNode.getSentMessages().forEach((guid, message) -> {
            message.getRecievers().forEach((address -> {
                chatNode.getSender().send(message.getMessage(), address, true);
            }));
        });
    }
}
