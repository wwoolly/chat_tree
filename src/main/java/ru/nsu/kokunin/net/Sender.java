package ru.nsu.kokunin.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.JsonConverter;
import ru.nsu.kokunin.utils.Message;
import ru.nsu.kokunin.utils.MessageMetadata;
import ru.nsu.kokunin.utils.MessageType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender {
    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);

    private final ChatNode chatNode;
    private final DatagramSocket socket;

    private final JsonConverter jsonConverter = new JsonConverter();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public Sender(ChatNode chatNode, DatagramSocket socket) {
        this.chatNode = chatNode;
        this.socket = socket;
    }

    /**
     * Небезопасный метод. Не требует от контроллера подтверждения доставки сообщения. */
    public void send(Message message, InetSocketAddress receiver) {
        try {
            byte[] rawMessage = jsonConverter.toJson(message).getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(rawMessage, rawMessage.length, receiver);
            socket.send(sendPacket);
        } catch (IOException exc) {
            LOG.error("Error during sending message <{}> to \"{}\"", message, receiver, exc);
        }
    }

    public void broadcast(MessageMetadata message) {
        executor.submit(() -> chatNode.neighbours.keySet().forEach(neighbourAddress -> {
            if (neighbourAddress.equals(message.getSenderAddress())) {
                return;
            }

            if (message.isChecked()) {
                chatNode.addSentMessageToHistory(message.getMessage().getGUID(), message);
            }

            send(message.getMessage(), neighbourAddress);
        }));
    }

    public void sendACKMessage(String messageToConfirmGUID, InetSocketAddress receiverAddress) {
        Message confirmMessage = new Message(chatNode.name, messageToConfirmGUID, MessageType.ACK);
        send(confirmMessage, receiverAddress);
    }


}
