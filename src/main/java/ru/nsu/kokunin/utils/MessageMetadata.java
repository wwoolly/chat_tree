package ru.nsu.kokunin.utils;

import java.net.InetAddress;

public class MessageMetadata {
    private Message message;
    private InetAddress senderAddress;

    boolean isChecked;

    public MessageMetadata(Message message, InetAddress senderAddress) {
        this.message = message;
        this.senderAddress = senderAddress;

        isChecked = false;
    }
}
