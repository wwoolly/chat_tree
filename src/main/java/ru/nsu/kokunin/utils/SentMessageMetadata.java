package ru.nsu.kokunin.utils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SentMessageMetadata {
    private final Message message;
    private final List<InetSocketAddress> receiverAddresses = new CopyOnWriteArrayList<>();

    public SentMessageMetadata(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public boolean hasSentTo(InetSocketAddress receiverAddress) {
        if (receiverAddress == null) {
            return false;
        }

        return receiverAddresses.contains(receiverAddress);
    }

    public boolean hasNoReceivers() {
        return receiverAddresses.isEmpty();
    }

    public boolean removeReceiver(InetSocketAddress receiverAddress) {
        if (receiverAddress == null) {
            return false;
        }

        return receiverAddresses.remove(receiverAddress);
    }

    public boolean addReceiver(InetSocketAddress receiverAddress) {
        if (receiverAddress == null) {
            return false;
        }

        return receiverAddresses.add(receiverAddress);
    }
}
