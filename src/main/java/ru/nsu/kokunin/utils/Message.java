package ru.nsu.kokunin.utils;

import java.util.Objects;
import java.util.UUID;

public class Message {
    public static final String EMPTY_NAME = "@anonymous";
    public static final String EMPTY_TEXT = "@empty_message";
    public static final MessageType DEFAULT_TYPE = MessageType.ACK;

    private final String GUID;
    private final String name;
    private final String text;
    private final MessageType type;


    public Message(String name, String text, MessageType type) {
        this.GUID = UUID.randomUUID().toString();
        this.name = (name == null) ? EMPTY_NAME : name;
        this.text = (text == null) ? EMPTY_TEXT : text;
        this.type = (type == null) ? DEFAULT_TYPE : type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return GUID.equals(message.GUID) && name.equals(message.name) && text.equals(message.text) && type == message.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(GUID, name, text, type);
    }
}
