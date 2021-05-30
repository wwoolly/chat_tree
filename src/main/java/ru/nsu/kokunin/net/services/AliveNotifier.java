package ru.nsu.kokunin.net.services;

import ru.nsu.kokunin.ChatNode;

public class AliveNotifier implements Runnable {
    private final ChatNode chatNode;

    public AliveNotifier(ChatNode chatNode) {
        this.chatNode = chatNode;
    }

    @Override
    public void run() {
        chatNode.neighbours.keySet().forEach(chatNode.sender::sendAliveMessage);
    }
}
