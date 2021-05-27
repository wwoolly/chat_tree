package ru.nsu.kokunin.ui.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.ui.MessageRecipient;
import ru.nsu.kokunin.ui.MessageReader;
import ru.nsu.kokunin.ui.MessageWriter;
import ru.nsu.kokunin.utils.Message;

import java.util.concurrent.*;

public class ConsoleController {
    private static final long OUTPUT_MESSAGE_WRITING_INTERVAL = 1000L;
    private static final long WRITER_INITIALIZATION_DELAY = 3000L;

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleController.class);

    private final MessageReader consoleReader;

    private final MessageWriter consoleWriter;
    private final ConcurrentLinkedQueue<Message> outputMessagesQueue = new ConcurrentLinkedQueue<>();
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

    public void removeMessageRecipient(MessageRecipient recipient) {
        messageRecipients.remove(recipient);
    }

    public void start() {
        writerExecutor.scheduleAtFixedRate(() -> {
            while (!outputMessagesQueue.isEmpty()) {
                consoleWriter.outMessage(outputMessagesQueue.poll());
            }
            LOG.trace("Writer executor started");
        }, WRITER_INITIALIZATION_DELAY, OUTPUT_MESSAGE_WRITING_INTERVAL, TimeUnit.MILLISECONDS);

        LOG.info("Console controller started!");

        while(isActive) {
            while (consoleReader.hasMessage()) {
                Thread.onSpinWait();
                String messageText = consoleReader.readMessage();
                for (MessageRecipient recipient : messageRecipients) {
                    recipient.getMessage(messageText);
                }

                LOG.debug("Message <{}> read from console", messageText);
            }
        }
    }

    public void stop() {
        isActive = false;
        writerExecutor.shutdown();
        LOG.info("Console controller shut down");
    }

    public void outMessage(Message message) {
        if (message == null) {
            return;
        }

        LOG.debug("Message <{}> added to output queue", message);
        outputMessagesQueue.add(message);
    }
}
