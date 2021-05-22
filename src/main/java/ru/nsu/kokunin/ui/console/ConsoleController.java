package ru.nsu.kokunin.ui.console;

import ru.nsu.kokunin.ui.MessageGetter;
import ru.nsu.kokunin.ui.MessageReader;
import ru.nsu.kokunin.ui.MessageWriter;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsoleController {
    private final MessageReader consoleReader;

    private final MessageWriter consoleWriter;
    private final ScheduledExecutorService writerExecutor;

    private final ArrayList<MessageGetter> messageGetters = new ArrayList<>();
    private Boolean isInterrupted = false;


    public ConsoleController() {
        this.consoleReader = new ConsoleMessageReader();
        this.consoleWriter = new ConsoleMessageWriter();

        this.writerExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void addMessageGetter(MessageGetter getter) {
        messageGetters.add(getter);
    }

    public void start() {
        writerExecutor.scheduleAtFixedRate(consoleWriter, 0,3000, TimeUnit.MILLISECONDS);

        while(!isInterrupted) {
            if (consoleReader.hasMessage()) {
                String input = consoleReader.readMessage();

                //DEBUG
                consoleWriter.outMessage(input);

                for (MessageGetter g : messageGetters) {
                    g.getMessage(input);
                }
            }
        }

        writerExecutor.shutdown();
    }

    public void stop() {
        isInterrupted = true;
    }

    public void writeMessage(String msg) {
        consoleWriter.outMessage(msg);
    }
}
