package ru.nsu.kokunin.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.ChatNode;
import ru.nsu.kokunin.utils.JsonConverter;
import ru.nsu.kokunin.utils.Message;
import ru.nsu.kokunin.utils.ReceivedMessageMetadata;
import ru.nsu.kokunin.utils.MessageType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender {
    private static final String EMPTY_MESSAGE_BODY = "-";
    private static final Logger log = LoggerFactory.getLogger(Sender.class);

    private final ChatNode chatNode;
    private final DatagramSocket socket;

    private final JsonConverter jsonConverter = new JsonConverter();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public Sender(ChatNode chatNode, DatagramSocket socket) {
        this.chatNode = chatNode;
        this.socket = socket;
    }

    public void send(Message message, InetSocketAddress receiver, boolean isChecked) {
        try {
            byte[] rawMessage = jsonConverter.toJson(message).getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(rawMessage, rawMessage.length, receiver);
            socket.send(sendPacket);

            if (isChecked) {
                chatNode.registerSentMessage(message, receiver);
            }

        } catch (IOException exc) {
            log.error("Error during sending message <{}> to \"{}\"", message, receiver, exc);
        }
    }

    public void broadcast(Message message, InetSocketAddress senderAddress, boolean isChecked) {
        executor.submit(() -> chatNode.neighbours.keySet().forEach(neighbourAddress -> {
            if (neighbourAddress.equals(senderAddress)) {
                return;
            }

            send(message, neighbourAddress, isChecked);
        }));
    }

    public void broadcast(ReceivedMessageMetadata message) {
        broadcast(message.getMessage(), message.getSenderAddress(), message.isChecked());
    }


    //TODO вынести эти методы в отдельный интерфейс отправки в зависимости от типа. Неоч,
    //TODO что Receiver ничего не знает о типах сообщений, а сендер знает всё
    public void sendACKMessage(String messageToConfirmGUID, InetSocketAddress receiverAddress) {
        Message confirmMessage = new Message(chatNode.name, messageToConfirmGUID, MessageType.ACK);
        send(confirmMessage, receiverAddress, false);
    }

    public void sendAliveMessage(InetSocketAddress receiverAddress) {
        Message aliveMessage = new Message(chatNode.name, EMPTY_MESSAGE_BODY, MessageType.ALIVE);
        send(aliveMessage, receiverAddress, false);
    }

    public void sendGetMessage(InetSocketAddress receiverAddress) {
        Message getMessage = new Message(chatNode.name, EMPTY_MESSAGE_BODY, MessageType.GET);
        send(getMessage, receiverAddress, true);
    }

    public void sendUpdateMessage(String viceData, InetSocketAddress receiverAddress) {
        Message updateMesssage = new Message(chatNode.name, viceData, MessageType.UPDATE);
        send(updateMesssage, receiverAddress, true);
    }

    public void sendStartMessage(String viceData, InetSocketAddress receiverAddress) {
        Message startMessage = new Message(chatNode.name, viceData, MessageType.START);
        send(startMessage, receiverAddress, true);
    }
}
