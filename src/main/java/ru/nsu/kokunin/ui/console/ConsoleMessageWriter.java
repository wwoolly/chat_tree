package ru.nsu.kokunin.ui.console;

import ru.nsu.kokunin.ui.MessageWriter;
import ru.nsu.kokunin.utils.Message;


public class ConsoleMessageWriter implements MessageWriter {
    @Override
    public void outMessage(Message message) {
        System.out.println("<" + message.getName() + ">: " + message.getText());
    }

    @Override
    public void outServiceMessage(String message) {

        //TODO add format
//        System.out.println(String.format(message, ));
        System.out.println(message);
    }
}
