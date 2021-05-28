package ru.nsu.kokunin.net.handlers;

import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.MessageMetadata;

public interface MessageHandler {
    void handle(MessageMetadata message, ChatNode chatNode);
}
