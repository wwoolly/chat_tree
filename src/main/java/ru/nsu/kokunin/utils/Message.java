package ru.nsu.kokunin.utils;

import java.util.UUID;

public class Message {
    private final String name;
    private final String text;
    private final String GUID;
    private final MessageType type;


    public Message(String name, String text, MessageType type) {
        this.GUID = UUID.randomUUID().toString();
        this.name = name;
        this.text = text;
        this.type = type;
    }

    public String getGUID() {
        return GUID;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public MessageType getType() {
        return type;
    }
}
