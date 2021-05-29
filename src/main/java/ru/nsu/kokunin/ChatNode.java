package ru.nsu.kokunin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.net.Receiver;
import ru.nsu.kokunin.net.Sender;
import ru.nsu.kokunin.net.handlers.*;
import ru.nsu.kokunin.ui.MessageRecipient;
import ru.nsu.kokunin.utils.*;
import ru.nsu.kokunin.ui.console.ConsoleController;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ChatNode implements MessageRecipient {
    /**
     * Time constant in milliseconds.
     * Interval between message confirmation requests.
     * */
    public static final long CONFIRM_TIMEOUT = 2000;
    /**
     * Time constant in milliseconds.
     * Interval between sending this node is alive messages.
     * */
    public static final long KEEP_ALIVE_TIMEOUT = 20000;
    /**
     * Time constant in milliseconds.
     * Interval between alive messages of the current node.
     * */
    public static final long ALIVE_MESSAGES_INTERVAL = 5000;
    /**
     * Time constant in milliseconds.
     * Interval between current node read a console.
     * */
    public static final long MESSAGE_SCAN_INTERVAL = 2000;

    public static final long INITIALIZATION_DELAY = 10000;
    private static final int TIMER_TASKS_NUMBER = 3;

    private static final Logger LOG = LoggerFactory.getLogger(ChatNode.class);

    public final String name;
    public final int loseRatio;
    public final DatagramSocket socket;
    public final Sender sender;
    public final Receiver receiver;
    public final ConsoleController ioController;

    public InetSocketAddress vice = null;
    public final Map<InetSocketAddress, NeighbourMetadata> neighbours = new ConcurrentHashMap<>();

    //<GUID, Set<Receiver Address>> info for confirmation
    public final Map<String, SentMessageMetadata> sentMessages = new ConcurrentHashMap<>();
    public final Deque<String> receivedMessages = new ConcurrentLinkedDeque<>();

    private final ScheduledExecutorService timerExecutor = Executors.newScheduledThreadPool(TIMER_TASKS_NUMBER);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<MessageType, MessageHandler> handlers = new HashMap<>();

    public ChatNode(String name, int loseRatio, DatagramSocket socket) {
        this.loseRatio = loseRatio;
        this.name = name;
        this.socket = socket;

        this.ioController = new ConsoleController();
        this.sender = new Sender(this, socket);
        this.receiver = new Receiver(this, socket, loseRatio);
    }

    public void init() {
        if (loseRatio < 0 || loseRatio > 99) {
            LOG.error("Attempt to initialize ChatNode with the incorrect lose ratio. Value: {} not in [1,99]", loseRatio);
            throw new IllegalArgumentException("Incorrect lose ratio! Value: " + loseRatio + '!');
        }

        handlers.put(MessageType.CHAT, new ChatMessageHandler());
        handlers.put(MessageType.ACK, new ACKMessageHandler());
        handlers.put(MessageType.START, new StartMessageHandler());
        handlers.put(MessageType.ALIVE, new AliveMessageHandler());
        handlers.put(MessageType.UPDATE, new UpdateMessageHandler());

        ioController.addMessageRecipient(this);
        ioController.start();
    }

    public void handleMessage(ReceivedMessageMetadata message) {
        MessageType type = message.getMessage().getType();
        handlers.get(type).handle(message, this);
    }

    public void outChatMessage(ReceivedMessageMetadata message) {
        ioController.outMessage(message.getMessage());
    }

    public void registerSentMessage(Message message, InetSocketAddress receiver) {
        String guid = message.getGUID();
        SentMessageMetadata sentMessageMetadata;

        //synchronized?
        synchronized (sentMessages) {
            if (sentMessages.containsKey(guid)) {
                sentMessageMetadata = sentMessages.get(guid);
            } else {
                sentMessageMetadata = new SentMessageMetadata(message);
                sentMessages.put(guid, sentMessageMetadata);
            }
        }

        sentMessageMetadata.addReceiver(receiver);
    }

    public void registerNewNeighbour(InetSocketAddress neighbourAddress, NeighbourMetadata metadata) {
        LOG.info("Connected new node! Name: {}, Address: {}, Vice: {}",
                metadata.getName(), neighbourAddress, metadata.getVice());

        String serviceMessage = metadata.getName() + " joined the chat!";
        ioController.outServiceMessage(serviceMessage);
        updateNeighbourMetadata(neighbourAddress, metadata);
    }

    public void updateNeighbourMetadata(InetSocketAddress neighbourAddress, NeighbourMetadata metadata) {
        LOG.info("New data about neighbour \'{}\'; Address: {}; Vice: {},  saved",
                metadata.getName(), neighbourAddress, metadata.getVice());

        metadata.setAlive(true);
        neighbours.put(neighbourAddress, metadata);
    }

    void terminate() {
        executor.shutdown();
        timerExecutor.shutdown();
    }

    @Override
    public void getMessage(String messageText) {
        Message message = new Message(name, messageText, MessageType.CHAT);
        sender.broadcast(message, (InetSocketAddress) socket.getLocalSocketAddress(), true);
    }
}
