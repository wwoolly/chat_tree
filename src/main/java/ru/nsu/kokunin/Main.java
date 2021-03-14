package ru.nsu.kokunin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void parseArgs(String[] args) {
        String name = args[1];
        int port = Integer.parseInt(args[2]);
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
    }
}
