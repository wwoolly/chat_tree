package ru.nsu.kokunin.utils;

import java.util.UUID;

public class Message {
    private String name;
    private String text;
    private String GUID;



    public Message(String name, String text) {
        this.GUID = UUID.randomUUID().toString();
        this.name = name;
        this.text = text;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
