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
        chatNode.sentMessages.forEach((guid, message) -> {
            message.getRecievers().forEach((address -> {
                chatNode.sender.send(message.getMessage(), address, true);
            }));
        });
    }
}
