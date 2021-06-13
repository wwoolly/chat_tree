package ru.nsu.kokunin.net.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public abstract class MessageHandler {
    protected final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public void handle(ReceivedMessageMetadata message, ChatNode chatNode) {
        //в теории никогда не null
        if (message.getSenderAddress() == null) {
            NullPointerException exc = new NullPointerException("Unknown message sender address!");
            log.error("Unknown message sender address! Message GUID: {}, sender: {}",
                    message.getMessage().getGUID(), message.getMessage().getName(), exc);
            throw exc;
        }
    }
}
