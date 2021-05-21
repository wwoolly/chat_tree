package ru.nsu.kokunin;

import ru.nsu.kokunin.node.TreeNode;
import ru.nsu.kokunin.services.Listener;
import ru.nsu.kokunin.services.Reader;
import ru.nsu.kokunin.services.Sender;

public class Main {
    public static TreeNode parseArgs(String[] args) {
        String name = args[0];
        int port = Integer.parseInt(args[1]);
        int loseRatio = Integer.parseInt(args[2]);

        return new TreeNode(name, port, loseRatio);
    }

    public static void main(String[] args) {
        Sender sender = new Sender(parseArgs(args));
        Reader reader = new Reader();
        Listener listener = new Listener();

        Chat chat = new Chat(reader, sender, listener);
        chat.init();
    }
}
