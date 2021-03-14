package ru.nsu.kokunin;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void parseArgs(String[] args) {
        String name = args[1];
        int port = Integer.parseInt(args[2]);
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            byte[] pingData = "PING".getBytes(StandardCharsets.UTF_8);
            InetAddress hostAdress = InetAddress.getLocalHost();
            datagramSocket.connect(hostAdress, port);
            DatagramPacket sendPacket = new DatagramPacket(pingData, pingData.length, hostAdress, port);
            datagramSocket.send(sendPacket);
            byte[] receiveData = new byte[8];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            datagramSocket.setSoTimeout(200);
            datagramSocket.receive(receivePacket);

//            а может вот так???
//            InetAddress.getLocalHost().isReachable(8000);
        } catch (SocketTimeoutException e) {
//            return /*true*/;
        } catch (IOException e) {
//            return /*false*/;
        }


    }

    public static void main(String[] args) {

    }
}
