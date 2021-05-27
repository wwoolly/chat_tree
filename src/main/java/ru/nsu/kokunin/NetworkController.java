package ru.nsu.kokunin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.kokunin.utils.JsonConverter;
import ru.nsu.kokunin.utils.Message;
import ru.nsu.kokunin.utils.MessageMetadata;
import ru.nsu.kokunin.utils.MessageType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NetworkController {
    private static final int BUFFER_SIZE = 524288;
    private static final Logger LOG = LoggerFactory.getLogger(NetworkController.class);

    private final DatagramSocket socket;

    private Map<String, Message> messages;

    private volatile boolean isActive = true;

    public NetworkController(DatagramSocket socket) {
        this.socket = socket;
    }

    public void handleMessage(MessageMetadata message) {

    }
}
