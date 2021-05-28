package ru.nsu.kokunin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class AddressStringPacker {
    private final static int IP_ADDRESS_OFFSET = 1;
    private final static Logger LOG = LoggerFactory.getLogger(AddressStringPacker.class);

    public static String packAddress(InetSocketAddress addressToPack) {
        if (addressToPack.getAddress() == null) {
            LOG.warn("Trying to pack zero address: [{}]", addressToPack);
            return null;
        }

        String ipAddress = addressToPack.getAddress().getHostAddress();
        int port = addressToPack.getPort();

        return "[" + ipAddress + "]:" + port;
    }

    public static InetSocketAddress unpackAddress(String addressToUnpack) {
        int lastByteOfIpAddress = addressToUnpack.indexOf(']');
        String ipAddress = addressToUnpack.substring(IP_ADDRESS_OFFSET, lastByteOfIpAddress);
        int port = Integer.parseInt(addressToUnpack.substring(lastByteOfIpAddress + 1));

        InetSocketAddress unpackedAddress = null;
        try {
            unpackedAddress = new InetSocketAddress(ipAddress, port);
        } catch (SecurityException exc) {
            LOG.error("Security error during parsing address: [{}]", addressToUnpack, exc);
        } catch (IllegalArgumentException exc) {
            LOG.error("Trying to parse incorrect address: [{}]", addressToUnpack, exc);
        }

        return unpackedAddress;
    }
}
