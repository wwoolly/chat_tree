package ru.nsu.kokunin.utils;

import java.net.InetSocketAddress;

public class ReceivedMessageMetadata {
    private Message message;
    private InetSocketAddress senderAddress;

    /**
     * Требуется ли ждать узлу подтверждения доставки
     * этого сообщения
     *
     * Учитывается только при отправке сообщения,
     * при приёме считаем, что Handler'ы определяют
     * это опираясь на тип сообщения.
     * */
    private boolean isChecked = false;

    public ReceivedMessageMetadata(Message message, InetSocketAddress senderAddress) {
        this.message = message;
        this.senderAddress = senderAddress;
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
