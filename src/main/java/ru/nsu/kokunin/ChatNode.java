package ru.nsu.kokunin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.net.Receiver;
import ru.nsu.kokunin.net.Sender;
import ru.nsu.kokunin.ui.MessageRecipient;
import ru.nsu.kokunin.utils.MessageType;
import ru.nsu.kokunin.utils.NeighbourData;
import ru.nsu.kokunin.ui.console.ConsoleController;
import ru.nsu.kokunin.utils.Message;
import ru.nsu.kokunin.utils.MessageMetadata;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;
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

    private final String name;
    private final int loseRatio;
    private final DatagramSocket socket;
    private final Sender sender;
    private final Receiver receiver;
    private final ConsoleController ioController;
    private final List<NeighbourData> neighbours = new CopyOnWriteArrayList<>();

    //<GUID, message>
    private final Map<String, MessageMetadata> messages = new ConcurrentHashMap<>();

    private final ScheduledExecutorService timerExecutor = Executors.newScheduledThreadPool(TIMER_TASKS_NUMBER);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ChatNode(String name, int loseRatio, DatagramSocket socket) {
//        this.neighbours =
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

        ioController.addMessageRecipient(this);
        ioController.start();
    }

    public void handleMessage(MessageMetadata message) {
        MessageType type = message.getMessage().getType();

        if (type.equals(MessageType.CHAT)) {
            message.setChecked(true);
//            messages.put(message.getMessage().getGUID(), message);
//            String outFormatted message
//            ioController.outMessage();
        }
    }

    public void addSentMessageToHistory(String messageGUID, InetSocketAddress receiverAddress) {

    }

    void terminate() {
        executor.shutdown();
        timerExecutor.shutdown();
    }

    public List<NeighbourData> getNeighbours() {
        return neighbours;
    }

    @Override
    public void getMessage(String messageText) {
        Message message = new Message(name, messageText, MessageType.CHAT);
        MessageMetadata messageMetadata = new MessageMetadata(message, (InetSocketAddress) socket.getLocalSocketAddress());
        messageMetadata.setChecked(true);
//        messages.put(message.getGUID(), messageMetadata);
        sender.broadcast(messageMetadata);
    }
}
