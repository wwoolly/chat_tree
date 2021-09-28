package ru.nsu.kokunin.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonConverterTest {
    private final Message testMessage;
    private final String testJson;
    {
        JsonConverter jsonConverter = new JsonConverter();
        testMessage = new Message("John", "Hi all", MessageType.CHAT);
        testJson = jsonConverter.toJson(testMessage);
    }

    @Test
    void toJsonTest() {
        JsonConverter jsonConverter = new JsonConverter();
        String convertedString = jsonConverter.toJson(testMessage);

        Assertions.assertEquals(testJson, convertedString);
    }

    @Test
    void fromJsonTest() {
        JsonConverter jsonConverter = new JsonConverter();
        Message convertedMessage = jsonConverter.fromJson(testJson, Message.class);

        Assertions.assertEquals(testMessage, convertedMessage);
    }

    @Test
    void nullArgToJsonTest() {
        JsonConverter jsonConverter = new JsonConverter();

        
    }
}