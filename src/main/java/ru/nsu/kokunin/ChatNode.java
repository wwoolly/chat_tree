package ru.nsu.kokunin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.net.Receiver;
import ru.nsu.kokunin.net.Sender;
import ru.nsu.kokunin.net.handlers.*;
import ru.nsu.kokunin.net.services.AliveNotifier;
import ru.nsu.kokunin.net.services.NeighbourChecker;
import ru.nsu.kokunin.net.services.Repeater;
import ru.nsu.kokunin.ui.MessageRecipient;
import ru.nsu.kokunin.utils.*;
import ru.nsu.kokunin.ui.console.ConsoleController;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ChatNode implements MessageRecipient {
    /**
     * Time constant in milliseconds.
     * Interval between message confirmation requests.
     * */
    private static final long CONFIRM_TIMEOUT = 2000;
    /**
     * Time constant in milliseconds.
     * Interval between sending alive messages of this node.
     * */
    private static final long ALIVE_NOTIFY_INTERVAL = 5000;
    /**
     * Time constant in milliseconds.
     * Time after which the neighbour is considered dead if there isn't
     * received Alive messages from it.
     * */
    private static final long ALIVE_NEIGHBOUR_LIMIT = 20000;

    private static final long INITIALIZATION_DELAY = 10000;
    private static final int TIMER_TASKS_NUMBER = 3;

    private static final Logger log = LoggerFactory.getLogger(ChatNode.class);

    private final String name;
    private final int loseRatio;
    private final DatagramSocket socket;
    private final Sender sender;
    private final Receiver receiver;
    private final ConsoleController ioController;

    private InetSocketAddress vice = null;
    private final Map<InetSocketAddress, NeighbourMetadata> neighbours = new ConcurrentHashMap<>();

    //<GUID, Set<Receiver Address>> info for confirmation
    private final Map<String, SentMessageMetadata> sentMessages = new ConcurrentHashMap<>();
    private final Deque<String> receivedMessages = new ConcurrentLinkedDeque<>();

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
            log.error("Attempt to initialize ChatNode with the incorrect lose ratio. Value: {} not in [1,99]", loseRatio);
            throw new IllegalArgumentException("Incorrect lose ratio! Value: " + loseRatio + '!');
        }

        timerExecutor.scheduleAtFixedRate(
                new Repeater(this),
                INITIALIZATION_DELAY,
                CONFIRM_TIMEOUT, MILLISECONDS
        );
        timerExecutor.scheduleAtFixedRate(
                new AliveNotifier(this),
                INITIALIZATION_DELAY,
                ALIVE_NOTIFY_INTERVAL, MILLISECONDS
        );
        timerExecutor.scheduleAtFixedRate(
                new NeighbourChecker(this),
                INITIALIZATION_DELAY,
                ALIVE_NEIGHBOUR_LIMIT, MILLISECONDS
        );
        log.info("Timer executors in ChatNode started");

        handlers.put(MessageType.CHAT, new ChatMessageHandler());
        handlers.put(MessageType.ACK, new ACKMessageHandler());
        handlers.put(MessageType.START, new StartMessageHandler());
        handlers.put(MessageType.ALIVE, new AliveMessageHandler());
        handlers.put(MessageType.UPDATE, new UpdateMessageHandler());
        handlers.put(MessageType.GET, new GetMessageHandler());

        executor.submit(receiver);

        if (!neighbours.isEmpty()) {
            vice = neighbours.keySet().iterator().next();
            String viceStr = AddressStringPacker.packAddress(vice);
            neighbours.forEach((k, v) -> {
                sender.sendStartMessage(viceStr, k);
            });
        } else {
            vice = null;
        }

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
        log.info("Connected new node! Name: {}, Address: {}, Vice: {}",
                metadata.getName(), neighbourAddress, metadata.getVice());

        String serviceMessage = metadata.getName() + " joined the chat!";
        ioController.outServiceMessage(serviceMessage);
        updateNeighbourMetadata(neighbourAddress, metadata);
    }

    public void updateNeighbourMetadata(InetSocketAddress neighbourAddress, NeighbourMetadata metadata) {
        log.info("New data about neighbour '{}'; Address: {}; Vice: {},  saved",
                metadata.getName(), neighbourAddress, metadata.getVice());

        metadata.setAlive(true);
        neighbours.put(neighbourAddress, metadata);
    }

    public Message createMessage(String text, MessageType type) {
        return new Message(name, text, type);
    }

    public InetSocketAddress getVice() {
        return vice;
    }

    public void setVice(InetSocketAddress vice) {
        this.vice = vice;
    }

    public Sender getSender() {
        return sender;
    }

    public Map<InetSocketAddress, NeighbourMetadata> getNeighbours() {
        return neighbours;
    }

    public Map<String, SentMessageMetadata> getSentMessages() {
        return sentMessages;
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
