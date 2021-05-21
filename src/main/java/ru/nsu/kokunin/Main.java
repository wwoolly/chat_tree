package ru.nsu.kokunin;

import ru.nsu.kokunin.node.NodeData;
import ru.nsu.kokunin.node.TreeNode;
import ru.nsu.kokunin.services.Listener;
import ru.nsu.kokunin.services.Reader;
import ru.nsu.kokunin.services.Sender;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static NodeData parseArgs(String[] args) {
        String name = args[0];
        int port = Integer.parseInt(args[1]);
        int loseRatio = Integer.parseInt(args[2]);

        return new NodeData(name, port, loseRatio);
    }

    public static void main(String[] args) {
        Sender sender = new Sender(new TreeNode(parseArgs(args)));
        Reader reader = new Reader();
        Listener listener = new Listener();

        Chat chat = new Chat(reader, sender, listener);
        chat.init();
    }
}
