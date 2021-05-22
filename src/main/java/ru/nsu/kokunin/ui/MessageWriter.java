package ru.nsu.kokunin.ui;

public interface MessageWriter extends Runnable {
    public void outMessage(String message);
}
