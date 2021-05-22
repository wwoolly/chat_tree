package ru.nsu.kokunin.ui.console;

import ru.nsu.kokunin.ui.MessageWriter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsoleMessageWriter implements MessageWriter {

    private final Queue<String> outputMessagesQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        while (!outputMessagesQueue.isEmpty()) {
            System.out.println("MESSAGE: " + outputMessagesQueue.poll());
        }
    }

    @Override
    public void outMessage(String message) {
        if (message == null) {
            return;
        }

        outputMessagesQueue.add(message);
    }
}
