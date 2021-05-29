package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;

public interface MessageHandler {
    void handle(ReceivedMessageMetadata message, ChatNode chatNode);
}
