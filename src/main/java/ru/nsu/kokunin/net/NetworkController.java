package ru.nsu.kokunin.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.MessageRecipient;
import ru.nsu.kokunin.utils.Message;
import ru.nsu.kokunin.utils.MessageMetadata;
import ru.nsu.kokunin.utils.NeighbourData;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetworkController {
    private static final int BUFFER_SIZE = 524288;
    private static final Logger LOG = LoggerFactory.getLogger(NetworkController.class);

    private final Sender sender;
    private final Receiver receiver;
    private final MessageRecipient recipient;

    /**
     * <GUID, Message>
     */
    private final List<NeighbourData> neighbours = new CopyOnWriteArrayList<>();
    private final Map<String, MessageMetadata> messages = new ConcurrentHashMap<>();

    private final DatagramSocket socket;

    private volatile boolean isActive = true;

    public NetworkController(MessageRecipient recipient, DatagramSocket socket, int loseRatio) {
        this.sender = new Sender(this, socket, neighbours);
        this.receiver = new Receiver(this, socket, loseRatio);
        this.socket = socket;
    }

    public void cacheNewMessage(MessageMetadata message) {
//        messages.put(message.getGUID(), messageMetadata);
    }

    public void handleMessage(MessageMetadata message) {

    }

    public void addSentMessageToHistory(String messageGUID, InetSocketAddress receiverAddress) {

    }
}
