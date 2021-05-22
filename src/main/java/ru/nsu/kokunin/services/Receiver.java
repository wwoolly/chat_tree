package ru.nsu.kokunin.services;

import ru.nsu.kokunin.utils.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class Receiver {
    private static final int BUFFER_SIZE = 524288;

    private final DatagramSocket socket;
    private final byte[] receiverBuffer = new byte[BUFFER_SIZE];

    public Receiver(DatagramSocket socket) {
        this.socket = socket;
    }

    public byte[] receiveMessage() {
        byte[] rawMessage = null;
        DatagramPacket packetToReceive = new DatagramPacket(receiverBuffer, BUFFER_SIZE);

        try {
            socket.receive(packetToReceive);
            rawMessage = packetToReceive.getData();
        } catch (IOException exc) {
            //TODO: add exception handler
            exc.printStackTrace();
        }

        return rawMessage;
    }
}
