package ru.nsu.kokunin.ui.console;

import ru.nsu.kokunin.ui.MessageReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ConsoleMessageReader implements MessageReader {
    private final Scanner inputScanner;

    public ConsoleMessageReader() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        this.inputScanner = new Scanner(reader);
    }

    @Override
    public boolean hasMessage() {
        return inputScanner.hasNextLine();
    }

    @Override
    public String readMessage() {
        return inputScanner.nextLine();
    }
}
