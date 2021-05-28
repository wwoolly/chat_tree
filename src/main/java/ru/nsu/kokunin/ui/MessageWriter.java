package ru.nsu.kokunin.ui;

import ru.nsu.kokunin.utils.Message;

public interface MessageWriter {
    void outMessage(Message message);
    void outServiceMessage(String message);
}
