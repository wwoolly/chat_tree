package ru.nsu.kokunin.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class MessageTest {

    @Test
    void creationTest() {
        final String
                testName = "name",
                testText = "text";
        final MessageType testType = Message.DEFAULT_TYPE;
        Message message = new Message(testName, testText, testType);

        Assertions.assertNotNull(message.getGUID());
        Assertions.assertEquals(testName, message.getName());
        Assertions.assertEquals(testText, message.getText());
        Assertions.assertEquals(testType, message.getType());
    }

    @Test
    void nullMessageCreationTest() {
        Message message = new Message(null, null, null);

        Assertions.assertNotNull(message.getGUID());
        Assertions.assertEquals(Message.EMPTY_NAME, message.getName());
        Assertions.assertEquals(Message.EMPTY_TEXT, message.getText());
        Assertions.assertEquals(Message.DEFAULT_TYPE, message.getType());
    }

    @Test
    void messageFieldsNotNullTest() {
        Message message = new Message(null, null, null);

        Assertions.assertNotNull(message.getName());
        Assertions.assertNotNull(message.getText());
        Assertions.assertNotNull(message.getType());
    }
}