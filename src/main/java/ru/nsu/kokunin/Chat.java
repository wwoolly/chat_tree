package ru.nsu.kokunin;

import ru.nsu.kokunin.services.Listener;
import ru.nsu.kokunin.services.Reader;
import ru.nsu.kokunin.services.Sender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Chat {
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

    public static final long INITIALIZATION_DELAY = 50000;
    private static final int TIMER_TASKS_NUMBER = 3;

    private final Reader reader;
    private final Sender sender;
    private final Listener listener;

//    private final Timer timer = new Timer();
    private final ScheduledExecutorService timerExecutor = Executors.newScheduledThreadPool(TIMER_TASKS_NUMBER);

    public Chat(Reader reader, Sender sender, Listener listener) {
        this.reader = reader;
        this.sender = sender;
        this.listener = listener;
    }

    private void init() {
        timerExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Aee zaglushka");
            }
        }, INITIALIZATION_DELAY, CONFIRM_TIMEOUT, TimeUnit.MILLISECONDS);
//        timer.schedule(, 0, CONFIRM_TIMEOUT);
//        timer.schedule(, 0, ALIVE_MESSAGES_INTERVAL);
//        timer.schedule(, 0, KEEP_ALIVE_TIMEOUT);
    }

    private void terminate() {
//        timer.cancel();
        timerExecutor.shutdownNow();
    }
}
