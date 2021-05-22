package ru.nsu.kokunin;

import ru.nsu.kokunin.node.TreeNode;

public class Main {
    public static TreeNode parseArgs(String[] args) {
        String name = args[0];
        int port = Integer.parseInt(args[1]);
        int loseRatio = Integer.parseInt(args[2]);

        return new TreeNode(name, port, loseRatio);
    }

    public static void main(String[] args) {
        Chat chat = new Chat(parseArgs(args));
        chat.init();
    }
}
