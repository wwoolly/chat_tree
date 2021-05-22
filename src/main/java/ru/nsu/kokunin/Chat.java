package ru.nsu.kokunin;

import ru.nsu.kokunin.node.TreeNode;
import ru.nsu.kokunin.services.Receiver;
import ru.nsu.kokunin.services.Sender;
import ru.nsu.kokunin.ui.MessageRecipient;
import ru.nsu.kokunin.ui.console.ConsoleController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Chat implements MessageRecipient {
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

    private TreeNode node;

    private Receiver receiver;
    private Sender sender;

//    private final Timer timer = new Timer();
    private final ScheduledExecutorService timerExecutor = Executors.newScheduledThreadPool(TIMER_TASKS_NUMBER);

    public Chat(TreeNode node) {
        this.node = node;

        this.receiver = new Receiver();
        this.sender = new Sender(node);
    }

    void init() {
        timerExecutor.scheduleAtFixedRate(() -> {
//                    System.out.println("Aee zaglushka");
            }, INITIALIZATION_DELAY, CONFIRM_TIMEOUT, TimeUnit.MILLISECONDS);
//        timer.schedule(, 0, CONFIRM_TIMEOUT);
//        timer.schedule(, 0, ALIVE_MESSAGES_INTERVAL);
//        timer.schedule(, 0, KEEP_ALIVE_TIMEOUT);
//        timerExecutor.scheduleAtFixedRate(() -> {
//                var consoleInputMessage = listener.getMessage();
//                if (consoleInputMessage != null) {
//                    System.out.println(consoleInputMessage);
//                }
//            }, INITIALIZATION_DELAY, MESSAGE_SCAN_INTERVAL, TimeUnit.MILLISECONDS);

        ConsoleController controller = new ConsoleController();
        controller.addMessageGetter(this);
        controller.start();
    }

    void terminate() {
//        timer.cancel();
        timerExecutor.shutdownNow();
    }

    @Override
    public void getMessage(String message) {
        node.cacheNewMessage(message);
    }
}
