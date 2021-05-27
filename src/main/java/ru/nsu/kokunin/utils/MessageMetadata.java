package ru.nsu.kokunin.utils;

import java.net.InetSocketAddress;

public class MessageMetadata {
    private Message message;
    private InetSocketAddress senderAddress;

    /**
     * Требуется ли проверка доставки сообщения
     * */
    boolean isChecked;

    public MessageMetadata(Message message, InetSocketAddress senderAddress) {
        this.message = message;
        this.senderAddress = senderAddress;

        isChecked = false;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Message getMessage() {
        return message;
    }

    public InetSocketAddress getSenderAddress() {
        return senderAddress;
    }
}
