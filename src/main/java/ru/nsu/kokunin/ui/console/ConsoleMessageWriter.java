package ru.nsu.kokunin.ui.console;

import ru.nsu.kokunin.ui.MessageWriter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsoleMessageWriter implements MessageWriter {
    @Override
    public void outMessage(String message) {
        System.out.println("MESSAGE: " + message);
    }
}
