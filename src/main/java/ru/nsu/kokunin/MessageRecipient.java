package ru.nsu.kokunin;

import ru.nsu.kokunin.utils.Message;

public interface MessageRecipient {
    public void getMessage(String messageText);
    public void getMessage(Message message);
}
