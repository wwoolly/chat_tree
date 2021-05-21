package ru.nsu.kokunin.ui.console;

import ru.nsu.kokunin.ui.MessageGetter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleController {
    Scanner inputScanner;
//    private ScheduledExecutorService reader;
    ArrayList<MessageGetter> messageGetters = new ArrayList<>();

    boolean isInterrupted = false;

    public ConsoleController() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        this.inputScanner = new Scanner(reader);
    }

    public void addMessageGetter(MessageGetter getter) {
        messageGetters.add(getter);
    }

    private String scanMessage() {
        if (inputScanner.hasNextLine()) {
            return inputScanner.nextLine();
        }
        return null;
    }

    public void start() {
        while(!isInterrupted) {
            String input = scanMessage();
            if (input != null) {
                System.out.println("Символы считаны");
                for (MessageGetter g : messageGetters) {
                    g.getMessage(input);
                }
            }
        }
    }
}
