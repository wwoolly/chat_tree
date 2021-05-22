package ru.nsu.kokunin.ui.console;

import ru.nsu.kokunin.ui.MessageRecipient;
import ru.nsu.kokunin.ui.MessageReader;
import ru.nsu.kokunin.ui.MessageWriter;

import java.util.concurrent.*;

public class ConsoleController implements Runnable {
    private static final long OUTPUT_MESSAGE_WRITING_INTERVAL = 1000L;
    private static final long WRITER_INITIALIZATION_DELAY = 3000L;

    private final MessageReader consoleReader;

    private final MessageWriter consoleWriter;
    private final ConcurrentLinkedQueue<String> outputMessagesQueue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService writerExecutor;

    private volatile boolean isActive = true;
    private final CopyOnWriteArrayList<MessageRecipient> messageRecipients = new CopyOnWriteArrayList<>();


    public ConsoleController() {
        this.consoleReader = new ConsoleMessageReader();
        this.consoleWriter = new ConsoleMessageWriter();
        this.writerExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void addMessageRecipient(MessageRecipient recipient) {
        messageRecipients.add(recipient);
    }

    public void start() {
        writerExecutor.scheduleAtFixedRate(this, WRITER_INITIALIZATION_DELAY, OUTPUT_MESSAGE_WRITING_INTERVAL, TimeUnit.MILLISECONDS);

        while(isActive) {
            while (consoleReader.hasMessage()) {
                Thread.onSpinWait();
                String messageText = consoleReader.readMessage();
                for (MessageRecipient recipient : messageRecipients) {
                    recipient.getMessage(messageText);
                }

                //DEBUG
                outMessage(messageText);
            }
        }
    }

    public void stop() {
        isActive = false;
        writerExecutor.shutdown();
    }

    public void outMessage(String message) {
        if (message == null) {
            return;
        }

        outputMessagesQueue.add(message);
    }

    @Override
    public void run() {
        while (!outputMessagesQueue.isEmpty()) {
            consoleWriter.outMessage(outputMessagesQueue.poll());
        }
    }
}
